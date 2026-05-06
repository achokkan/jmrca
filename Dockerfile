FROM eclipse-temurin:25-jdk-alpine AS builder
WORKDIR /workspace
COPY pom.xml mvnw ./
COPY .mvn .mvn
COPY src src

# Make Maven wrapper executable and build the application
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Stage 2: Minimal runtime environment
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Add a non-root user for security
RUN addgroup -S jmcra && adduser -S jmcra -G jmcra
USER jmcra

# Copy the built fat JAR from the builder stage
COPY --from=builder /workspace/target/java-ms-cr-agent-1.0.0-SNAPSHOT.jar app.jar

# Expose Spring WebFlux port
EXPOSE 8080

# Run with Java 25 preview features enabled (for Structural Concurrency JEPs)
ENTRYPOINT ["java", "--enable-preview", "-jar", "app.jar"]
