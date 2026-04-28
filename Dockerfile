# Build
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -q
COPY src ./src
RUN mvn clean package -DskipTests -q

## Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S garage && adduser -S garage -G garage
COPY --from=build /app/target/*.jar app.jar
USER garage
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]