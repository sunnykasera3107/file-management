FROM maven:3.9-eclipse-temurin-21

WORKDIR /home/app

COPY target/*.jar log-app.jar

CMD ["java", "-jar", "log-app.jar"]
