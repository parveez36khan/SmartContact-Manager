# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the WAR file from the target directory to the container
COPY target/smartContactManager-0.0.1-SNAPSHOT.war /app/smartContactManager.war

# Expose the port on which the Spring Boot app will run
EXPOSE 8080

# Command to run the Spring Boot application (WAR with embedded Tomcat)
CMD ["java", "-jar", "/app/smartContactManager.war"]
