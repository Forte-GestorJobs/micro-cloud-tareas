FROM openjdk:17-jdk-buster
VOLUME /tmp
VOLUME /usr/local/share/ca-certificates
EXPOSE 8082
ARG JAR_FILE=target/micro-cloud-tareas-spring-boot.jar
ADD ${JAR_FILE} app.jar
COPY Zscaler_Root_CA.crt /usr/local/share/ca-certificates/Zscaler_Root_CA.crt
RUN update-ca-certificates

ENTRYPOINT ["java","-jar","/app.jar"]