FROM openjdk:17-jdk-slim

COPY build/libs/*.jar gbgokgok-api.jar
ENTRYPOINT ["java", "-jar", "/gbgokgok-api.jar"]