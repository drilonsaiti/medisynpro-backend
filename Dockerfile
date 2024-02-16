# Use the official Gradle image with OpenJDK-21
FROM ubuntu:latest AS build
RUN apt-get update
RUN apt-get install openjdk-21-jdk -y

# Copy local code to the container image
COPY . .
RUN chmod +x ./gradlew
# Build a release artifact
RUN ./gradlew bootJar --no-daemon

# Use OpenJDK-21 for runtime
FROM openjdk:21-jdk-slim

# Expose port 9091
EXPOSE 9091

# Copy the jar file from build stage
COPY --from=build /build/libs/MediSyncPro-1.jar app.jar


# Run the application
ENTRYPOINT ["java","-jar","app.jar"]
