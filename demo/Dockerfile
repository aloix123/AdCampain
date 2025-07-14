# Stage 1: Build the app
FROM gradle:8.5-jdk21 AS builder

# Set working directory
WORKDIR /home/app

# Copy only necessary files for caching
COPY gradle gradle
COPY gradlew build.gradle settings.gradle ./

# Copy the source code
COPY src src

# Build the fat JAR (for Spring Boot or Shadow plugin)
RUN ./gradlew bootJar --no-daemon

# Stage 2: Run the app using Amazon Corretto
FROM amazoncorretto:21

WORKDIR /app

# Copy the jar from the builder stage
COPY --from=builder /home/app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
