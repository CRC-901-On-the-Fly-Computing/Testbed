# Setting up the Development Environment

This guide describes the steps needed to prepare your development environment for building the POC applications.

## System Requirements

The following software must be installed on your development machine:

* NodeJS (with NPM): The tested version was v14.18.2
* Java SDK: 1.8 is mandatory
* Docker: v20.10.21 has been tested
* OS: Debian-like Linux OR Windows with WSL2

This is the only "tested" configuration. As for other operating systems:

* Mac OS and other Linux distributions MAY work, but require (a lot of) tinkering
* Native Windows (without WSL) likely will NOT work - just use WSL instead

You *really* should use the specified versions for everything, or you'll almost certainly run into (tons of) issues.

### Installing NodeJS (and the Node Package Manager)

Since the ``website`` service uses an ancient TypeScript/Angular version, you MUST install a similarly-old, compatible Node version.

The recommended (tested) version is [14.18.2](https://nodejs.org/ja/blog/release/v14.18.2/)). An easy way to install it is by using [nvm](https://github.com/nvm-sh/nvm):

* ``curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.2/install.sh | bash`` (check their README for newer versions)
* Restart the terminal (if using WSL: Restart the VM via ``wsl --shutdown``)
* ``nvm install 14.18.2``
* ``nvm use 14.18.2`` (should be done automatically)

Test that it works:

  * ``node -v`` (should display "v14.18.2")
  * ``npm -v`` (should display "6.14.15")
  * ``npx -v`` (should display "6.14.15")

### Installing a Java Development Kit

Only Java 8 is supported. Newer versions, particularly the "default-jdk" on Ubuntu, will *not* work. You MUST install any version 8 SDK, such as:

* ``sudo apt install nvidia-openjdk-8-jre`` on Kali Linux
* ``sudo apt install openjdk-8-jdk`` on Debian and Ubuntu

You can probably find the right package via ``sudo apt update && sudo apt list | grep openjdk-8`` (or similar queries).

If you need to manage multiple Java versions, use ``update-java-alternatives`` (or ``sudo update-alternatives --config java``). Details [here](https://askubuntu.com/questions/315646/update-java-alternatives-vs-update-alternatives-config-java).

### Installing Docker

The exact version shouldn't matter here, but it's important that the API version is compatible with your Nexus instance (we used ``v2``).

If you can ``docker login your.nexus.server.url`` and ``docker push`` images to it, it's probably fine. Otherwise, install the recommended version.

### Windows Subsystem for Linux

While the process is generally the same as on Linux, Windows users must take extra care:

* You'll have to use [WSL](https://docs.microsoft.com/en-us/windows/wsl/), ideally with Ubuntu/Kali (other distributions are untested)
* Use the latest version (WSL2). If you still have WSL1 installed, do ``wsl --update`` and never look back
* Make sure you have [assigned enough memory to ``Vmmem``, the WSL VM service](https://learn.microsoft.com/en-us/windows/wsl/wsl-config)
  * With less than 4GB, the VM will almost certainly crash at some point during the build process
  * The standard location of the configuration file is ``C:\Users\<UserName>\.wslconfig``
  * An example file can be found here: [.wslconfig](assets/.wslconfig)
* You CANNOT install Docker inside the VM normally (i.e., via ``apt install docker``)
  * Download [Docker Desktop for Windows](https://docs.docker.com/desktop/install/windows-install/) and enable the WSL2 integration
  * Type ``sudo docker`` in the WSL environment afterwards (*without* installing it via ``apt``) to see if it works

Generally, expect a lot of "weird" issues whenever the assigned RAM is too low. Most aren't apparent since the host system will be unaffected.
