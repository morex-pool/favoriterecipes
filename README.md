# Manage Favorite Recipes #

The Recipe Management is an application to manage users recipes.
User able to insert, update, delete, and get recipes.

#### Implemented based on Java 17, Spring Boot 3.2.6, and it uses H2 database ####

## The software that you need on your machine for running this application ##

+ [Docker](https://www.docker.com) in case you want to run the application in a docker container
+ [Git](https://git-scm.com) to get the source from web to your machine
+ Java (17+) to be able to have proper Java JDK to run the application inside it
+ Maven to get the dependencies, compile and run the application
+ your favorite IDE in case you want to see the project and code structure

## Application Architecture explanation ##

#### Overview ####

The application is a standalone Java application designed to manage users' favorite recipes. It provides functionalities
to add, update, remove, and fetch recipes, with additional capabilities to filter recipes based on various criteria. The
application follows a typical Spring Boot architecture with RESTful endpoints, service layers, repository layers, and
security configurations.

#### Layers and Components ####

Controller Layer: The controller layer handles HTTP requests and sends responses back to the client. It contains methods
mapped to specific endpoints to manage recipe operations.

> RecipeController: Manages endpoints for recipe operations.

- @GetMapping("/recipes/{id}"): Fetches a recipe by its ID.
- @GetMapping("/recipes"): Fetches all recipes.
- @PostMapping("/recipes"): Adds a new recipe.
- @PutMapping("/recipes"): Updates an existing recipe.
- @DeleteMapping("/recipes/{id}"): Deletes a recipe by its ID.
- @GetMapping("/recipes/filter"): Filters recipes based on the specified criteria.

Service Layer: The service layer contains the business logic of the application. It processes data received from the
controller and interacts with the repository layer.

> RecipeService: Provides methods to manage recipes.

- getById(Long id): Fetches a recipe by its ID.
- getAllRecipes(): Fetches all recipes.
- save(RecipeDtoRequest recipeDtoRequest): Saves a new recipe.
- update(RecipeDtoRequest recipeDtoRequest): Updates an existing recipe.
- delete(Long id): Deletes a recipe by its ID.
- filterRecipes(Boolean isVegetarian, Integer servings, List<String> includeIngredients, List<String>
  excludeIngredients, String instructions): Filters recipes based on the specified criteria.

> Repository Layer: The repository layer interacts with the database. It uses Spring Data JPA to perform CRUD operations
> and custom queries.

- RecipeRepository: Extends JpaRepository to provide CRUD operations.
- IngredientRepository: Extends JpaRepository to manage ingredients.
    - findByName(String name): Finds an ingredient by its name.
    - findAllByName(List<String> names): Finds all ingredients by their names.

> Related to Ingredients that are used in recipes, there are related Rest APIs to manage them


> Security Layer: The security layer manages authentication and authorization. It ensures that only authenticated users
> can access certain endpoints.

- MyUserDetailsService: Implements UserDetailsService to load user-specific data.
- SecurityConfig: Configures HTTP security, authentication, and authorization settings.

> Logging Aspect: The logging aspect uses Spring AOP to log API calls. It intercepts method calls and logs the user
> details, method name, and endpoint.

- LoggingAspect: Logs API calls.
    - Uses @Before and @AfterReturning advices to log method execution details.

> Example Use Cases

The application can handle various search requests such as:

Fetching all vegetarian recipes.
Fetching recipes that serve a specific number of people and include certain ingredients.
Excluding recipes with specific ingredients and containing specific instructions.

> Integration Tests

Integration tests are designed to test the interaction between different layers of the application, ensuring that the
components work together as expected.

- RecipeControllerTest: Contains tests for various endpoints using MockMvc.
    - testGetRecipeById(): Tests fetching a recipe by its ID.
    - testGetAllRecipes(): Tests fetching all recipes.
    - testCreateRecipe(): Tests adding a new recipe.
    - testUpdateRecipe(): Tests updating an existing recipe.
    - testDeleteRecipe(): Tests deleting a recipe by its ID.
    - testFilterRecipes(): Tests filtering recipes based on criteria.

> Running the Application

The application can be run using Maven with the following command:

``mvn spring-boot:run``

This command compiles the project, resolves dependencies, and starts the Spring Boot application.

> Summary: This architecture ensures a clear separation of concerns, with each layer handling specific responsibilities.
> The use of Spring Boot simplifies configuration and setup, while Spring Data JPA provides an easy way to interact with
> the database. Security is managed using Spring Security, and AOP is used for logging purposes. Integration tests
> ensure
> the correctness of the application's functionality.

# To run the application: #

1- Please open the terminal and go to application folder root.

2- Run `mvn clean install` command

3- Run `mvn spring-boot:run` command

4- In your browser path execute http://localhost:8080 , then you should be able to see a login page that protect Rest
APIs against not authorized user.

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

To build: docker build -t favoriterecipes-0.0.1-SNAPSHOT.jar .

To run: docker run -p 8080:8080 favoriterecipes-0.0.1-SNAPSHOT.jar

# Note #

For test the Rest APIs, you can use Postman collection file that placed in root of the project

Postman collection file: Manage-Recipes.postman_collection.json


