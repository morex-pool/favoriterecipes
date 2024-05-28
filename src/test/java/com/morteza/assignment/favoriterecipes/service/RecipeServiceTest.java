package com.morteza.assignment.favoriterecipes.service;

import com.morteza.assignment.favoriterecipes.dto.RecipeDtoRequest;
import com.morteza.assignment.favoriterecipes.dto.RecipeDtoResponse;
import com.morteza.assignment.favoriterecipes.entity.Ingredient;
import com.morteza.assignment.favoriterecipes.entity.Recipe;
import com.morteza.assignment.favoriterecipes.entity.User;
import com.morteza.assignment.favoriterecipes.exceptions.CustomException;
import com.morteza.assignment.favoriterecipes.repository.IngredientRepository;
import com.morteza.assignment.favoriterecipes.repository.RecipeRepository;
import com.morteza.assignment.favoriterecipes.security.MyUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private RecipeService recipeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Mock authentication
        User user = new User();
        user.setId(1L);
        user.setUsername("user1");
        user.setPassword("user11");
        user.setRoles("ROLE_USER");
        UserDetails myUserDetails = new MyUserDetails(user);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(myUserDetails, null, myUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void testGetAllRecipes() {
        // Given
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setUserId(1L);
        List<Recipe> recipes = Collections.singletonList(recipe);
        when(recipeRepository.findAll()).thenReturn(recipes);
        when(recipeRepository.findByUserId(1L)).thenReturn(recipes);

        // When
        List<RecipeDtoResponse> result = recipeService.getAllRecipes();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
    }

    @Test
    void testGetById() {
        // Given
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setUserId(1L);
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));

        // When
        RecipeDtoResponse result = recipeService.getById(1L);

        // Then
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void testSave() {
        // Given
        RecipeDtoRequest recipeDtoRequest = new RecipeDtoRequest();
        when(recipeRepository.findByNameAndUserId(anyString(), anyLong())).thenReturn(Optional.empty());
        when(recipeRepository.save(any())).thenReturn(new Recipe());

        // When
        RecipeDtoResponse result = recipeService.save(recipeDtoRequest);

        // Then
        assertThat(result).isNotNull();
    }

    @Test
    void testSave_DuplicateName() {

        // Given
        RecipeDtoRequest recipeDtoRequest = new RecipeDtoRequest();
        recipeDtoRequest.setName("recipe1");
        Recipe recipe = new Recipe();
        recipe.setName("recipe1");
        recipe.setUserId(1L);

        when(recipeRepository.findByNameAndUserId(anyString(), anyLong())).thenReturn(Optional.of(recipe));

        // When / Then
        CustomException exception = assertThrows(CustomException.class, () -> recipeService.save(recipeDtoRequest));
        assertThat(exception).hasMessageContaining("Duplicate recipe name is not allowed!");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void testUpdate() {
        // Given
        RecipeDtoRequest recipeDtoRequest = new RecipeDtoRequest();
        recipeDtoRequest.setId(1L);
        recipeDtoRequest.setName("recipe1");
        Recipe recipe = new Recipe();
        recipe.setName("recipe1");
        recipe.setUserId(1L);
        recipe.setIngredients(List.of(new Ingredient(1L, "ingredient1", null)));

        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(recipeRepository.save(any())).thenReturn(recipe);

        // When
        RecipeDtoResponse result = recipeService.update(recipeDtoRequest);

        // Then
        assertThat(result).isNotNull();
    }

    @Test
    void testDelete() {
        // Given
        Recipe recipe = new Recipe();
        recipe.setName("recipe1");
        recipe.setUserId(1L);
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));

        // When
        recipeService.delete(1L);

        // Then
        verify(recipeRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFilterRecipes() {
        // Givens
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setIsVegetarian(true);
        recipe.setServings(1);
        recipe.setUserId(1L);
        recipe.setInstructions("... oven ...");
        recipe.setIngredients(List.of(new Ingredient(1L, "Tomato", null)));
        when(recipeRepository.findAll((Specification<Recipe>) any())).thenReturn(Collections.singletonList(recipe));

        // When
        List<RecipeDtoResponse> result1 = recipeService.filterRecipes(true, 1,
                List.of("Tomato"), List.of("Chicken"), "oven");
        // Then
        assertThat(result1).isNotEmpty();
    }
}
