FROM maven:3.9-eclipse-temurin-21

WORKDIR /home/app

COPY target/*.jar files-app.jar

CMD ["java", "-jar", "files-app.jar"]