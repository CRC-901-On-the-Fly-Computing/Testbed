FROM openjdk:8-jdk-stretch
VOLUME /tmp

ENV Z3_VERSION "4.5.0"

USER root

# install system
RUN apt-get update -qq -y && \
#    apt-get install -y openjdk-8-jdk && \

# fix certificate issues
    apt-get install -y ca-certificates-java && \
    update-ca-certificates -f && \

# install make
    apt-get install -y binutils g++ make ant && \
    apt-get install -y python && \

# clean up
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*


# download, compile and install Z3
RUN Z3_DIR="$(mktemp -d)" && \
    cd "$Z3_DIR" && \
    wget -qO- https://github.com/Z3Prover/z3/archive/z3-${Z3_VERSION}.tar.gz | tar xz --strip-components=1 && \
    python scripts/mk_make.py --java && \
    cd build && \
    make && \
    make install && \
    cd / && \
    rm -rf "$Z3_DIR"

ARG JAR_FILE
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
