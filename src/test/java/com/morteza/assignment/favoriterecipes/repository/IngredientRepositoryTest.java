package com.morteza.assignment.favoriterecipes.repository;

import com.morteza.assignment.favoriterecipes.entity.Ingredient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class IngredientRepositoryTest {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Test
    public void testFindByName() {
        // Given
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Salt");

        // Save ingredient to the repository
        ingredientRepository.save(ingredient);

        // When
        Optional<Ingredient> foundIngredient = ingredientRepository.findByName("Salt");

        // Then
        assertThat(foundIngredient).isPresent();
        assertThat(foundIngredient.get().getName()).isEqualTo("Salt");
    }

    @Test
    public void testFindAllByName() {
        // Given
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setName("Sugar");
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setName("Pepper");
        Ingredient ingredient3 = new Ingredient();
        ingredient3.setName("Garlic");

        // Save ingredients to the repository
        ingredientRepository.saveAll(Arrays.asList(ingredient1, ingredient2, ingredient3));

        // When
        List<String> names = Arrays.asList("Sugar", "Pepper");
        List<Ingredient> foundIngredients = ingredientRepository.findAllByName(names);

        // Then
        assertThat(foundIngredients).hasSize(2);
        assertThat(foundIngredients).extracting(Ingredient::getName).containsExactlyInAnyOrder("Sugar", "Pepper");
    }
}

