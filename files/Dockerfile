FROM maven:3.9-eclipse-temurin-21

WORKDIR /home/app

# COPY pom.xml .

# RUN mvn dependency:go-offline

COPY target/*.jar files-app.jar

# RUN mvn clean package -DskipTests

CMD ["java", "-jar", "files-app.jar"]