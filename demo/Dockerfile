# Stage 1: Build the app using Gradle with JDK 21
FROM gradle:8.5-jdk21 AS build

WORKDIR /home/app

COPY gradle gradle
COPY gradlew build.gradle settings.gradle ./
COPY src src

# Avoid toolchain issues by disabling it or using system JDK
RUN ./gradlew bootJar --no-daemon

# Stage 2: Run the app with Amazon Corretto 21
FROM amazoncorretto:21

WORKDIR /app

COPY --from=build /home/app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
