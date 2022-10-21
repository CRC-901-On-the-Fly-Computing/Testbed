FROM ubuntu:18.04

LABEL maintainer="saman@mail.uni-paderborn.de"

## Install packages
RUN apt-get update \
 && apt-get install -yq --no-install-recommends \
    	build-essential \
    	curl \
    	git \
    	openjdk-8-jdk \
 && rm -rf /var/lib/apt/lists/*

## Install gradle
RUN apt-get update \
 && apt-get install -yq --no-install-recommends \
    	software-properties-common \
 && add-apt-repository ppa:cwchien/gradle \
 && apt-get purge -yq \
    	software-properties-common \
 && apt-get update \
 && apt-get install -qf \
 && apt-get install -yq --no-install-recommends --allow-unauthenticated gradle \
 && rm -rf /var/lib/apt/lists/*


## We can get the bootup script from git repository (a)
## or copy them as files beside the image (b)
## or as volume for development (c)

## a. Clone the bootup scripts in current directory
## b. Or copy from the disk
# COPY . .

## c. Set the dev folder as a volume with a command like this:
## docker run -e SERVICE='catalano' -td --name task1-c task1-image \
## -v your-dev-path/executor-bootup:/sede


COPY custom.vars /temp/custom.vars
COPY init.sh /temp/init.sh

RUN mkdir -p /sede \
 && mkdir -p /temp \
 && chmod +x /temp/init.sh

## Running Port
EXPOSE 8000


## Finally run the script inside the container
CMD ["/sede/run.sh"]
