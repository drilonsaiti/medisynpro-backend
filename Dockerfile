# Use the official Gradle image with OpenJDK-21
FROM gradle:8.6-jdk21 AS build

# Copy local code to the container image
COPY . .

# Build a release artifact
RUN gradle clean build -x test

# Use OpenJDK-21 for runtime
FROM openjdk:21-jdk-slim

# Copy the jar file from build stage
COPY --from=build /build/libs/com.example-0.0.1-SNAPSHOT.jar app.jar

# Expose port 9091
EXPOSE 9091

# Run the application
ENTRYPOINT ["java","-jar","app.jar"]
