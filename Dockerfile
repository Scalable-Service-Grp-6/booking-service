# Use official OpenJDK image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the jar from target (after mvn package)
COPY target/booking-service-0.0.1-SNAPSHOT.jar app.jar


EXPOSE 9096

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
