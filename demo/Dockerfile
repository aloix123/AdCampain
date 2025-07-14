
# Use Amazon Corretto 21 as base image
FROM amazoncorretto:21

# Optional: temp dir for JVM
VOLUME /tmp

# Copy the fat JAR built by Gradle into the image
COPY  demo/build/libs/backend-0.0.1-SNAPSHOT.jar app.jar

# Expose app port
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
