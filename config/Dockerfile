FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/config-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8012

ENTRYPOINT ["java", "-jar", "app.jar"]
