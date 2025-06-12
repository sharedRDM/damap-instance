FROM registry.access.redhat.com/ubi8/openjdk-17:1.16

ARG INSTANCE_TYPE=backend
ARG INSTANCE_NAME
ENV LANGUAGE='en_US:en'

WORKDIR /app

COPY instances/${INSTANCE_TYPE}/${INSTANCE_NAME}/src ./src
COPY instances/${INSTANCE_TYPE}/${INSTANCE_NAME}/pom.xml .

USER 0
RUN mvn clean package -DskipTests

# copy the built application
RUN mkdir -p /deployments && \
    if [ -d target/quarkus-app ]; then \
        cp -r target/quarkus-app/* /deployments/; \
    else \
        cp target/*.jar /deployments/app.jar; \
    fi

USER 1001

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "exec java -jar /deployments/quarkus-run.jar"] 