# Use an Alpine Linux image with OpenJDK 21 for the build stage
FROM alpine:latest AS build

# Install OpenJDK 21
RUN apk add --no-cache openjdk21

# Copy local code to the container image
COPY . .

# Build a release artifact
RUN ./gradlew  --no-daemon

# Use an Alpine Linux image with OpenJDK 21 for the runtime stage
FROM alpine:latest

# Install OpenJDK 21
RUN apk add --no-cache openjdk21

EXPOSE 8080

# Copy the jar file from the build stage
COPY --from=build /build/libs/MediSyncPro-1.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
