# step1:build
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY SpringProjectTeam5/pom.xml .
COPY SpringProjectTeam5/src ./src
RUN mvn clean package -DskipTests

# step2:run
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

