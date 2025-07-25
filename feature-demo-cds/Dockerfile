# Perform the extraction in a separate builder container
FROM bellsoft/liberica-openjre-debian:24-cds AS builder
WORKDIR /builder
# This points to the built jar file in the target folder
ARG JAR_FILE=target/*.jar
# Copy the jar file to the working directory and rename it to application.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]