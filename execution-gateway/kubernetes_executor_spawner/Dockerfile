FROM adoptopenjdk/openjdk11:x86_64-alpine-jdk-11.0.4_11

VOLUME /tmp

ARG JAR_FILE
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
