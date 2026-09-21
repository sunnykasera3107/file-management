FROM maven:3.9-eclipse-temurin-21

WORKDIR /home/app

# COPY pom.xml .

# RUN mvn dependency:go-offline

COPY target/*.jar users-app.jar

# RUN mvn clean package -DskipTests

CMD ["java", "-jar", "users-app.jar"]
