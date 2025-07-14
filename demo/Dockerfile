FROM ubuntu:latest
# Use official OpenJDK image
FROM openjdk:21-jdk-alpine

# Add a volume pointing to /tmp (optional, helps with some JVM temp files)
VOLUME /tmp

# Copy the jar file
COPY target/*.jar app.jar

# Expose the port (optional)
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "/gradle-wrapper.jar"]
