# Use a base image with Java 17 installed
FROM adoptopenjdk/openjdk17:alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled JAR file into the container at /app
COPY target/favoriterecipes-0.0.1-SNAPSHOT.jar /app/favoriterecipes-0.0.1-SNAPSHOT.jar

# Expose the port your application runs on (assuming it runs on port 8080)
EXPOSE 8080

# Specify the command to run your application
CMD ["java", "-jar", "favoriterecipes-0.0.1-SNAPSHOT.jar"]