# Use a base image with JDK pre-installed
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the WAR file into the container
COPY target/smartContactManager-0.0.1-SNAPSHOT.war /app/smartContactManager.war

# Command to run your Spring Boot application
CMD ["java", "-jar", "/app/smartContactManager.war"]
