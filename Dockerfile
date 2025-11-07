# ---------- Stage 1: Build the JAR ----------
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
# Extract layered JAR structure
RUN java -Djarmode=layertools -jar target/*.jar extract


# ---------- Stage 2: Run the JAR ----------
#FROM openjdk:21
#FROM eclipse-temurin:21-jre-alpine
FROM gcr.io/distroless/java21
WORKDIR /app
#COPY --from=build /app/target/olx-Login-0.0.1-SNAPSHOT.jar olx-login.jar
# Copy extracted layers
COPY --from=build /app/dependencies/ ./
COPY --from=build /app/snapshot-dependencies/ ./
COPY --from=build /app/spring-boot-loader/ ./
COPY --from=build /app/application/ ./
EXPOSE 8089
#ENTRYPOINT ["java", "-jar", "olx-login.jar"]
#CMD [ "olx-login.jar" ]
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]

