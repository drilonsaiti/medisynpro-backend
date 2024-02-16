# Use the official Gradle image with OpenJDK
FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-21-jdk -y

# Copy local code to the container image
COPY . .

# Grant executable permissions to Gradle wrapper script
RUN chmod +x gradlew

# Build a release artifact with skipping tests
RUN ./gradlew bootJar --no-daemon

# Use OpenJDK for runtime
FROM openjdk:latest

# Expose port 9091
EXPOSE 9091

# Copy the jar file from the build stage
COPY --from=build /app/build/libs/MediSyncPro-1.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
