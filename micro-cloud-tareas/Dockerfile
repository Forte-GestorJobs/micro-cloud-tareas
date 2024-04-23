FROM openjdk:17-jdk-buster
VOLUME /tmp
VOLUME /usr/local/share/ca-certificates
EXPOSE 8082
ARG JAR_FILE=target/micro-cloud-tareas-spring-boot.jar
ADD ${JAR_FILE} app.jar
COPY Zscaler_Root_CA.crt /usr/local/share/ca-certificates/Zscaler_Root_CA.crt
RUN update-ca-certificates

ENV MCT_DBHOST "localhost"
ENV MCT_DBNAME "postgres"
ENV MCT_DBPORT 5432
ENV MCT_DBPWD "123"
ENV MCT_DBUSER "postgres"
ENV AWS_ACCESS_KEY_ID "AKIATXSB4LWL4RQMWK7W"
ENV AWS_SECRET_KEY "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"
ENV AWS_REGION "eu-west-2"


ENTRYPOINT ["java","-jar","/app.jar"]