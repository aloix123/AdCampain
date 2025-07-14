
# Use Amazon Corretto 21 as base image
FROM amazoncorretto:21

# Add a volume pointing to /tmp (optional)
VOLUME /tmp

# Copy the application JAR file into the image
COPY target/*.jar app.jar

# Expose the port your app runs on (e.g., 8080)
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app.jar"]
