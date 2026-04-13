FROM eclipse-temurin:25-jdk
ARG JAR_FILE=target/*.jar
COPY ./target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 8081
