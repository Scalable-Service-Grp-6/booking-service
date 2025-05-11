# ===== Stage 1: Build the JAR =====
FROM maven:3.9.4-eclipse-temurin-17 as builder

# Set the working directory
WORKDIR /build

# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the project files
COPY . .

# Build the project
RUN mvn clean package -DskipTests

# ===== Stage 2: Create the runtime image =====
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR from the builder stage
COPY --from=builder /build/target/booking-service-0.0.1-SNAPSHOT.jar app.jar

# Expose the service port
EXPOSE 9096

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
