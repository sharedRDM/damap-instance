# syntax=docker/dockerfile:1
#==============================================================================
# STAGE 1: BUILD STAGE
# Build the application using Maven in an Alpine-based container
#==============================================================================
FROM maven:3.9.5-eclipse-temurin-17-alpine AS builder

ARG BUILD_HOME=/home/app
ARG BUILD_PROFILE=postgres

# instance configuration: JKU, MUG, TUG
ARG INSTANCE_NAME

# Optional: override DAMAP base version
ARG DAMAP_BASE_VERSION

# build directories with proper permissions for non-root user
RUN mkdir $BUILD_HOME && \
    mkdir -p $BUILD_HOME/.m2/repository && \
    chown -R 1000:0 $BUILD_HOME

# Switch to non-root user for security
USER 1000
WORKDIR $BUILD_HOME

# Copy parent POM first
COPY pom.xml /pom.xml

# copies from instances/${INSTANCE_NAME}/ directory
COPY instances/${INSTANCE_NAME}/src ./src
COPY instances/${INSTANCE_NAME}/pom.xml .

# Copy Maven settings.xml for GitHub Packages authentication
COPY .m2/settings.xml $BUILD_HOME/.m2/settings.xml

# Maven repository volume for caching dependencies
VOLUME ["/home/app/.m2/repository"]

# build the application
RUN if [ -n "$DAMAP_BASE_VERSION" ]; then \
      mvn -Duser.home=$BUILD_HOME -B package -DskipTests -Dquarkus.profile=${BUILD_PROFILE} -Ddamap.base.version=$DAMAP_BASE_VERSION; \
    else \
      mvn -Duser.home=$BUILD_HOME -B package -DskipTests -Dquarkus.profile=${BUILD_PROFILE}; \
    fi

#==============================================================================
# STAGE 2: RUNTIME STAGE
# Create a lightweight container with only the required dependencies to run the app
#==============================================================================
FROM rockylinux:8.5 AS runner

ARG JAVA_PACKAGE=java-17-openjdk-headless
ARG RUN_JAVA_VERSION=1.3.8
ARG BUILD_HOME=/home/app

ENV LANG='en_US.UTF-8' LANGUAGE='en_US:en'

# install runtime dependencies and set up deployment directory
RUN dnf install -y openssl tzdata-java curl ca-certificates ${JAVA_PACKAGE} && \
    dnf clean all -y && \
    # Create deployment directory with proper permissions
    mkdir /deployments && \
    chown 1001 /deployments && \
    chmod "g+rwX" /deployments && \
    chown 1001:root /deployments && \
    # Download and install run-java script for optimized JVM startup
    curl https://repo1.maven.org/maven2/io/fabric8/run-java-sh/${RUN_JAVA_VERSION}/run-java-sh-${RUN_JAVA_VERSION}-sh.sh -o /deployments/run-java.sh && \
    chown 1001 /deployments/run-java.sh && \
    chmod 540 /deployments/run-java.sh && \
    # Optimize JVM random number generation for faster startup
    echo "securerandom.source=file:/dev/urandom" >> /etc/alternatives/jre/lib/security/java.security

# configure JVM options for Quarkus application
ENV JAVA_OPTIONS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager -Duser.home=/deployments"

# copy compiled application from builder stage
COPY --from=builder $BUILD_HOME/target/quarkus-app/lib/ /deployments/lib/
COPY --from=builder $BUILD_HOME/target/quarkus-app/*.jar /deployments/
COPY --from=builder $BUILD_HOME/target/quarkus-app/app/ /deployments/app/
COPY --from=builder $BUILD_HOME/target/quarkus-app/quarkus/ /deployments/quarkus/

# expose application port
EXPOSE 8080

# user 1001 is standard for OpenShift and Kubernetes environments
USER 1001

ENTRYPOINT [ "/deployments/run-java.sh" ]