# Building the POC services

This guide describes the process for turning the POC microservices from source code into Docker images that are ready to be deployed.

## Overview

Here's what the build process entails:

1. Configure the build parameters
1. Build each microservice as a Docker image
1. Push the images to a Docker Registry
1. Verify that the artifacts are available in said registry

## Prerequisites

For the purposes of this guide, we assume that you:

* already set up your development environment [as per our instructions](set-up-dev-environment.md)
* have [prepared a Docker Registry that is hosted via Nexus Repository Manager](setup-nexus-repository-with-nginx.md)
* can access said repository via the Docker CLI(through a nginx reverse proxy
* have sufficient hardware resources to successfully complete the build (4 GB RAM, disk space on Nexus, network bandwidth, etc.)
* use a single node (and not several) to host your Kubernetes control plane, herein called the "Kubernetes master node"

We've done our best to provide guidance on each of the required procedures, but it may still take quite some work on your end. 

## Setting Build Parameters

### Filling in gradle.properties

Before running a successful build, you need to configure the Gradle tasks (via ``gradle.properties`` file):

* Set ``userNexusBaseUrlWithoutProtocol`` to the IP or URL of your Nexus instance
* Fill out the ``userNexusUsername`` and ``userNexusPassword`` with your Nexus credentials
* Do NOT include the protocol prefix (e.g., ``https://`` or docker API suffix (e.g., ``v2``)
* The user must have write access to the ``sfb901`` repository, via Docker CLI (test via ``docker login your-nexus-url``)

Leave everything else as it is. In particular, don't change any of the ``sfbNexus*`` fields.

### Docker Authentication

Use the credentials from ``gradle.properties`` to authenticate with BOTH Nexus instances:

* ``docker login your.nexus.server.url`` (to push build artifacts to your Nexus repository)
* ``docker login nexus.cs.upb.de`` (to download certain dependencies from our Nexus repository)

More information can be found in the [official Docker documentation](https://help.sonatype.com/repomanager3/nexus-repository-administration/formats/docker-registry/docker-authentication).

### Running the Build Script

The build script (``build.sh``) will prompt you to input the following required parameters:

1. The *base* URL of your Nexus instance, in a format that can be passed to ``docker login`` (e.g., ``nexus.cs.upb.de`` - WITHOUT ``https://``)
3. The name of the Docker registry (Nexus repository of type ``docker (hosted)``) - MUST be ``sfb901`` or it likely won't work
4. The URL (or IP address) of your Kubernetes master node, to be embedded into the docker images

You may have to rebuild whenever any of these parameters changes. You *definitely* have to rebuild when the Kubernetes master node changes.

### Using build-inputs.txt (optional)

Pro Tip: If you don't want to enter the same values again whenever things go wrong, or you want to automate the process, try this:

* Create a file, e.g. named ``build-inputs.txt``
* Enter the three parameters, one line at a time (with a final newline)
* Pipe this file to the build script directly, via *one* of the following:
  * `` cat build-inputs.txt | ./build.sh &> build.log`` (log results for easier debugging), OR
  * ``cat inputs.txt | ./build.sh`` (if you want to see what's happening in real time)

Since there are many ways that the build may fail, and you may have to run the script several times, this is the recommended approach.

Here's how such a ``build-inputs.txt`` file would look:

```txt
your-nexus-url
sfb901
your-kubernetes-master-node-url

```

Obviously, you need to use the real IP/URL of your Nexus/Kubernetes control plane. It's best to not change the repository name, for now.

## Performing the Build

The build script (``build.sh``) will take care of the following steps for you:

* Configure the ``URL`` constants in ``website/WebContent/src/app/app.url.json`` (this only works the first time you run the script, currently)
* Build each of the ten microservices by executing the respective Gradle task
* Build the frontend/website for production using NPM commands
* Push all of the resulting Docker images to the registry hosted by your Nexus instance

If need be, you can run the commands manually - just check what the script is doing and copy/paste the respective commands.

Pro Tip: You can press CTRL+C to skip the current step (when it says "building *target*"). This can save a lot of time when building repeatedly.

## Pushing the Docker Images to a Nexus Instance

This is already part of the ``build.sh`` script, but in case of failures you may want to run the script again, or run the same commands manually.

To verify, type ``sudo docker images`` and make sure that ALL of the following images have been built correctly (example output):

```
your.nexus.server.url/sfb901/website                       latest          b543327f0cea   8 hours ago     64.4MB
your.nexus.server.url/sfb901/proseco_configurator          latest          4465ab03837f   12 hours ago    466MB
your.nexus.server.url/sfb901/composition_analysis          latest          768e2d7b852f   13 hours ago    965MB
your.nexus.server.url/sfb901/kubernetes_executor_spawner   latest          2b6b05b0b347   14 hours ago    420MB
your.nexus.server.url/sfb901/policy_provider               latest          68ed3f946f45   14 hours ago    431MB
your.nexus.server.url/sfb901/otfp_registry_new             latest          8168b8267a69   14 hours ago    431MB
your.nexus.server.url/sfb901/buy_processor                 latest          5ca845560061   16 hours ago    442MB
your.nexus.server.url/sfb901/credential_issuer             latest          0cbcb6ed14f0   16 hours ago    431MB
your.nexus.server.url/sfb901/system_manager                latest          9ead995d09a8   16 hours ago    386MB
your.nexus.server.url/sfb901/review_board                  latest          ce3436a3a062   16 hours ago    433MB
your.nexus.server.url/sfb901/service_requester_new         latest          472f07250bab   16 hours ago    435MB
```

Once you see that all eleven images are present, you can push them to the docker registry (hosted by the Nexus):

``docker push your.nexus.server.url/sfb901/service.name:version``

where ``service.name`` is one of the above image names (e.g., ``website``) and ``version`` is (usually) ``latest``.

**Important:** Docker will refuse to connect unless you've authenticated with ``docker login your.nexus.server.url`` at least once. 

## Troubleshooting

We've observed several different failure modes that you may, unfortunately, also encounter.

### [Fatal Error] The element type "hr" must be terminated by the matching end-tag "</hr>".

This isn't actually a failure, but mentioned here because it's very misleadingly labeled "Fatal Error". What it *really* means is that a request sent to the Nexus returned an error code (usually 404, because the artifact didn't exist). There are many possible URL paths for a given artifact, which will be tried in a predefined order. You only need one *valid* response, so unless there are other problems this "error" can safely be ignored.

The message effectively means "I could not find the artifact at *some invalid URL* and will now check *next URL in the list*".

Eventually, Gradle will find the right URL and download the artifact. Hopefully.

### Could not initialize class org.codehaus.groovy.runtime.InvokerHelper

This compilation error indicates that your Java JDK version is incompatible. Install [a compatible Java 8 SDK](https://github.com/CRC-901-On-the-Fly-Computing/Testbed/blob/consolidated-docs/docs/how-to/set-up-dev-environment.md#installing-a-java-development-kit) instead.

### java.net.MalformedURLException: no protocol: /api

Errors of this type usually indicate that the first step in ``build.sh`` was performed incorrectly or simply failed.

First, check that the URL configuration is valid for your Kubernetes cluster: ``cat website/WebContent/src/app/app.url.json``

Expected output:

```json
{
  "BASE_PATH_RB": "http://your-kubernetes-master-node-url:32413/api",
  "BASE_PATH_SR": "http://your-kubernetes-master-node-url:31052/api",
  "BASE_PATH_UR": "http://your-kubernetes-master-node-url:31236/api",
  "BASE_PATH_CP": "http://your-kubernetes-master-node-url:30301",
  "MONITOR": "http://your-kubernetes-master-node-url:30080/elastic/kibana",
  "MONITOR_CPA": "http://your-kubernetes-master-node-url:31942/mm_cpa_checks/_search?size=10000",
  "POC_URL": "https://sfb901.uni-paderborn.de/projects/tools-and-demonstration-systems/tools-from-the-2nd-funding-period/proof-of-concept",
  "EXEC_GATEWAY_1": "http://your-kubernetes-master-node-url:30370/cmd/executors/ls/",
  "EXEC_GATEWAY_2": "http://your-kubernetes-master-node-url:30371/cmd/executors/ls/"
}
```

If the Kubernetes master IP is missing, that's why the error occured. Reset via ``git restore website/*``, then run the build script again.

This is always safe when rebuilding via ``build.sh``. The reason this can happen in the first place is this:

* When the first build process fails, the URL may be replaced with something invalid
* The ``build.sh`` script simply replaces a placeholder (``localhost``) value with your input
* Since this operation is *not* idempotent, running the script again may not update the app URL's correctly

### The Gradle build daemon disappeared unexpectedly

It probably fell victim to the [OOM Killer](https://www.kernel.org/doc/gorman/html/understand/understand016.html). If using WSL, [assign more memory to the VM](https://github.com/CRC-901-On-the-Fly-Computing/Testbed/blob/consolidated-docs/docs/how-to/set-up-dev-environment.md#windows-subsystem-for-linux). Otherwise, you'll need to upgrade your system.

### EBADENGINE: Unsupported engine

You're using an incompatible NodeJS version. To install one that will work, [follow the instructions provided here](https://github.com/CRC-901-On-the-Fly-Computing/Testbed/blob/consolidated-docs/docs/how-to/set-up-dev-environment.md#installing-nodejs-and-the-node-package-manager).

### ENOTEMPTY: Directory not empty

This error is related to ``EBADENGINE Unsupported engine`` and likely occurs at the same time, potentially sticking around if you've just upgraded.

In the latter case, you might want to get rid of any remaining artifacts that were created using the old (incompatible) Node version:

``rm -rf website/WebContent/node_modules``

Alternatively, do a ``npm install`` and ``npm run-scripts build`` in the ``website/WebContent`` directory (i.e., where ``package.json`` is located).

### 25104 Bus error

You (probably) just need more RAM. See: [The Gradle build daemon disappeared unexpectedly](#the-gradle-build-daemon-disappeared-unexpectedly)

### bash: /etc/profile: Input/output error

If you're very unlucky, this can happen after [#25104 Bus error](#25104-bus-error) when using WSL. [Assign more RAM to VMmem](https://github.com/CRC-901-On-the-Fly-Computing/Testbed/blob/consolidated-docs/docs/how-to/set-up-dev-environment.md#windows-subsystem-for-linux)), then completely restart the computer (restarting the VM probably isn't enough). Hopefully, the WSL installation will now work again. Otherwise, it might be easier to just reinstall WSL from scratch, or use a different distribution if you have any.

### Failed to compute cache key: "*some file or directory*" not found

This may indicate that you need more RAM and the system (or WSL VM, in particular) is failing.

See: [The Gradle build daemon disappeared unexpectedly](#the-gradle-build-daemon-disappeared-unexpectedly)

It may also mean that Docker failed to copy a non-existent file (or directory) into the image. Make sure the path exists (create empty directory).

### Process 'command 'docker'' finished with non-zero exit value 1

If this is the only error and the build itself seems to succeeed, chances are it's because the images couldn't be uploaded to the Nexus.

This may be due to a misconfiguration of the Nexus instance or (more likely) nginx proxy. Typical problems include:

* The maximum allowed request size is too small (setting it to 250 MB seems to work)
* The request may have timed out because the connection is too slow (retry and/or adjust timeout settings)
* In general, always check the nginx (and Nexus logs) to identify these kinds of problems

When in doubt, try using the ``docker`` CLI manually and copy the commands from ``build.sh``. 

If the error only affects some requests, part of the images may already have arrived and be stored in the Nexus' Docker Registry. Since artifacts are uploaded progressively and stored in smaller chunks, repeatedly re-running the build script should eventually finish the BLOB data.
