package com.morteza.assignment.favoriterecipes.repository;

import com.morteza.assignment.favoriterecipes.entity.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class RecipeRepositoryTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @BeforeEach
    public void setup() {
        recipeRepository.deleteAll(); // Clear the database before each test
    }

    @Test
    public void testFindByUserId() {
        // Given
        Recipe recipe1 = new Recipe();
        recipe1.setName("Spaghetti Bolognese");
        recipe1.setIsVegetarian(false);
        recipe1.setServings(4);
        recipe1.setInstructions("Cook spaghetti, add sauce.");
        recipe1.setUserId(1L);

        Recipe recipe2 = new Recipe();
        recipe2.setName("Vegetarian Lasagna");
        recipe2.setIsVegetarian(true);
        recipe2.setServings(6);
        recipe2.setInstructions("Layer vegetables, sauce, and pasta.");
        recipe2.setUserId(1L);

        Recipe recipe3 = new Recipe();
        recipe3.setName("Chicken Curry");
        recipe3.setIsVegetarian(false);
        recipe3.setServings(4);
        recipe3.setInstructions("Cook chicken, add curry sauce.");
        recipe3.setUserId(2L);

        // Save recipes to the repository
        recipeRepository.saveAll(Arrays.asList(recipe1, recipe2, recipe3));

        // When
        List<Recipe> foundRecipes = recipeRepository.findByUserId(1L);

        // Then
        assertThat(foundRecipes).hasSize(2);
        assertThat(foundRecipes).extracting(Recipe::getName).containsExactlyInAnyOrder("Spaghetti Bolognese", "Vegetarian Lasagna");
    }

    @Test
    public void testFindByNameAndUserId() {
        // Given
        Recipe recipe = new Recipe();
        recipe.setName("Beef Stew");
        recipe.setIsVegetarian(false);
        recipe.setServings(4);
        recipe.setInstructions("Cook beef, add vegetables and stew.");
        recipe.setUserId(3L);

        // Save recipe to the repository
        recipeRepository.save(recipe);

        // When
        Optional<Object> foundRecipe = recipeRepository.findByNameAndUserId("Beef Stew", 3L);

        // Then
        assertThat(foundRecipe).isPresent();
        assertThat(((Recipe) foundRecipe.get()).getName()).isEqualTo("Beef Stew");
        assertThat(((Recipe) foundRecipe.get()).getUserId()).isEqualTo(3L);
    }
}
