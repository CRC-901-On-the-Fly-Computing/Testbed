# Setting up a Nexus Repository

This document describes how you can set up a docker registry via the [Nexus repository manager](https://www.sonatype.com/products/repository-oss-download).

## Installation (Recommended)

Simply follow the instructions given in the Sonatype docs: [https://help.sonatype.com/repomanager3](https://help.sonatype.com/repomanager3)

## Alternative (UPB Setup)

If, for whatever reason, the above isn't working... here's how we set up our Nexus instance:

1. ``git clone https://github.com/sonatype/docker-nexus3.git``
1. ``cd docker-nexus3``
1. ``sudo docker build --rm=true --tag=sonatype/nexus3 --build-arg NEXUS_VERSION=3.40.1-01 --build-arg NEXUS_DOWNLOAD_SHA256_HASH=97f4e847e5c2ba714b09456f9fb5f449c7e89b2f0a2b8c175f36cc31f345774e . | tee docker-build.log``
1. Verify that it built successfully: ``sudo docker images`` (should show ``sonatype/nexus3`` with a recent "CREATED" time)
1. Run via ``sudo docker run -d --restart always -p 8081:8081 -p 5000:5000 --name nexus -v /data/sonatype-work/nexus3:/nexus-data sonatype/nexus3``
1. Verify it runs: ``sudo docker ps`` (should show ``sonatype/nexus3``)
1. The Nexus UI should now be available at ``your.nexus.server.url:8081``
1. Important: The version displayed in the UI should match the one submitted via the ``build-arg`` parameter (or something went horribly wrong)
1. Also make sure to test the docker registry (uses a different port): ``docker login your.nexus.server.url`` (omit protocol and port)

## Docker Registry Setup

After logging in to the Nexus UI:

1. Create a new repository (type: ``docker-images``, hosted)
1. Ours is called ``sfb901``, so you might want to copy that (to avoid issues with hardcoded repository names in some ancient gradle scripts)
1. You can try to access the registry via ``your.nexus.server.url/v2`` (assumes you're using a Docker v2 registry)

You have to be authenticated to use the docker registry (see [Docker docs](https://help.sonatype.com/repomanager3/nexus-repository-administration/formats/docker-registry/docker-authentication)).

## Reverse Proxy (nginx)

The ``docker`` CLI cannot deal with the URL format that the Nexus registry uses (apparently). Our workaround was to set up nginx:

1. Setup: You can use docker or just use the preinstalled nginx instance (via systemctl) on Ubuntu-like systems
1. One way to set up nginx is to use ``docker-compose`` - for an example, see [assets/docker-compose.yml](/assets/docker-compose.yml)
1. Configuration: See [Sonatype docs](https://help.sonatype.com/repomanager3/planning-your-implementation/run-behind-a-reverse-proxy#RunBehindaReverseProxy-Example:ReverseProxySSLTerminationatBasePath)
1. You may need to create a (self-signed) TLS certificate because Docker won't usually connect to an insecure registry
1. Make sure the maximum request size for nginx is at least 250MB (the build images will be pushed in large chunks and are otherwise rejected)
1. The Nexus UI should be available at port 443 (regular HTTPS scheme) behind the nginx server in order for ``docker login`` to work

If you need an example, feel free to use our (not at all production-ready or in any way secure) nginx configuration here: [assets/nginx.conf](/assets/nginx.conf).
