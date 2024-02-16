# Use the official Gradle image with OpenJDK
FROM gradle:latest AS build

RUN ./gradlew clean package -DskipTests
# Set the working directory inside the container
WORKDIR /app

# Copy local code to the container image
COPY . .

# Grant executable permissions to Gradle wrapper script
RUN chmod +x ./gradlew

# Build a release artifact
RUN ./gradlew bootJar --no-daemon

# Use OpenJDK for runtime
FROM openjdk:latest

# Set the working directory inside the container
WORKDIR /app

# Expose port 9091
EXPOSE 9091

# Copy the jar file from the build stage
COPY --from=build /app/build/libs/MediSyncPro-1.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
