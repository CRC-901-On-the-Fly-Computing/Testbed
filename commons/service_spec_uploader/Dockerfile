FROM adoptopenjdk/openjdk11:x86_64-alpine-jdk-11.0.4_11

VOLUME /tmp

ARG JAR_FILE
COPY ${JAR_FILE} app.jar

ARG MONGO_USER
ENV M_U=$MONGO_USER
ARG MONGO_PASSWORD
ENV M_PW=$MONGO_PASSWORD

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar", "$M_U", "$M_PW"]
