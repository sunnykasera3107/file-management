FROM maven:3.9-eclipse-temurin-21

WORKDIR /home/app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

CMD ["java", "-jar", "target/files-0.0.1-SNAPSHOT.jar"]
