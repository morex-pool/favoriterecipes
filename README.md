# Manage Favorite Recipes #

The Recipe Management is an application to manage users recipes.
User able to insert, update, delete, and get recipes.

#### Implemented based on Java 17 and Spring Boot 3.2.6 ####

## The software that you need on your machine for running this application ##

+ [Docker](https://www.docker.com)
+ [Git](https://git-scm.com)
+ a GitHub account
+ a recent JDK release (17+)
+ Maven
+ your favorite IDE

# To run the application: #

1- Please open the terminal and go to application folder root.

2- Run [mvn clean install] command

3- Run [mvn spring-boot:run] command

4- in your browser path execute http://localhost:8080 then you should be able to see a login page that protect Rest APIs against not authorized user.

5- following are users you can use to test the Rest APIs:

    - username: user1, password: user11
    - username: user2, password: user22
    - username: admin, password: adminadmin

# Application Rest API: #

Get: http://localhost:8080/ingredient To get all ingredients.

Get: http://localhost:8080/recipe To get all Recipes.

To see other APIs Please open Swagger-UI with following link.

# Application Rest API is accessible through Swagger-UI #

The application serves its Rest API through Swagger-UI with using below path:

http://localhost:8080/swagger-ui/index.html

# Containerization (To use the application in Docker) #

The Docker file is added in project to containerize the application.

# For test the Rest APIs, you can use Postman collection file that placed in root of the project #

Postman collection file: Manage-Recipes.postman_collection.json

To build: docker build -t favoriterecipes-0.0.1-SNAPSHOT.jar .

To run: docker run -p 8080:8080 favoriterecipes-0.0.1-SNAPSHOT.jar

