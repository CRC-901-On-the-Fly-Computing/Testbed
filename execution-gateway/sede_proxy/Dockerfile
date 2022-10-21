FROM adoptopenjdk/openjdk11:x86_64-alpine-jdk-11.0.4_11

ADD https://github.com/aminfa/SEDE.proxy/releases/download/v5.0/ExecutorProxy-5.0.jar /opt

ENV port=8080
ENTRYPOINT java -jar /opt/ExecutorProxy-5.0.jar "${port}"

