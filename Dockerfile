
# FASE 1 - Compilazione con Maven e Java 21
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -B -DskipTests package

# FASE 2 - Avvio con Java 21
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Porta utilizzata da Render
ENV PORT=8080

EXPOSE 8080

# Avvia Spring Boot con la porta assegnata da Render
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]
