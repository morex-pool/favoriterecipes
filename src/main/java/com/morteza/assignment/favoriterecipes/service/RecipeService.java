package com.morteza.assignment.favoriterecipes.service;

import com.morteza.assignment.favoriterecipes.dto.RecipeDtoRequest;
import com.morteza.assignment.favoriterecipes.dto.RecipeDtoResponse;
import com.morteza.assignment.favoriterecipes.entity.Ingredient;
import com.morteza.assignment.favoriterecipes.entity.Recipe;
import com.morteza.assignment.favoriterecipes.entity.User;
import com.morteza.assignment.favoriterecipes.enums.Comparison;
import com.morteza.assignment.favoriterecipes.exceptions.CustomException;
import com.morteza.assignment.favoriterecipes.exceptions.ResourceNotFoundException;
import com.morteza.assignment.favoriterecipes.mapper.IngredientMapper;
import com.morteza.assignment.favoriterecipes.mapper.RecipeMapper;
import com.morteza.assignment.favoriterecipes.repository.IngredientRepository;
import com.morteza.assignment.favoriterecipes.repository.RecipeRepository;
import com.morteza.assignment.favoriterecipes.security.MyUserDetails;
import com.morteza.assignment.favoriterecipes.service.specification.RecipeSpecification;
import com.morteza.assignment.favoriterecipes.util.Utils;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    public RecipeService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof MyUserDetails)) {
            try {
                throw new ResourceNotFoundException("logged in user");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return ((MyUserDetails) principal).getUser();
    }

    public List<RecipeDtoResponse> getAllRecipes() {
        User user = getCurrentUser();
        List<Recipe> recipes;
        if (user.getRoles().contains("ROLE_ADMIN")) {
            recipes = recipeRepository.findAll();
        } else {
            recipes = recipeRepository.findByUserId(user.getId());
        }
        List<RecipeDtoResponse> recipeDtoResponses = new ArrayList<>();
        recipes.forEach(r -> recipeDtoResponses.add(RecipeMapper.INSTANCE.toDto(r)));
        return recipeDtoResponses;
    }

    public RecipeDtoResponse getById(Long id) {
        User user = getCurrentUser();

        Optional<Recipe> recipe = recipeRepository.findById(id);
        if (recipe.isEmpty()) {
            throw new ResourceNotFoundException("recipe", id);
        }
        if (user.getRoles().contains("ROLE_ADMIN")) {
            return RecipeMapper.INSTANCE.toDto(recipe.get());
        }

        if (!user.getId().equals(recipe.get().getUserId())) {
            throw new ResourceNotFoundException("recipe", id);
        }

        return RecipeMapper.INSTANCE.toDto(recipe.get());
    }

    @Transactional
    public RecipeDtoResponse save(RecipeDtoRequest recipeDtoRequest) {
        Long userId = getCurrentUser().getId();
        // check to see if the recipe name is exist
        if (recipeRepository.findByNameAndUserId(recipeDtoRequest.getName(), userId).isPresent()) {
            throw new CustomException("Duplicate recipe name is not allowed!", HttpStatus.BAD_REQUEST);
        }
        Recipe recipe = RecipeMapper.INSTANCE.toEntity(recipeDtoRequest);
        recipe.setUserId(userId);
        recipe.setIngredients(prepareIngredients(recipeDtoRequest));

        Recipe result = recipeRepository.save(recipe);
        return RecipeMapper.INSTANCE.toDto(result);
    }

    private List<Ingredient> prepareIngredients(RecipeDtoRequest recipeDtoRequest) {

        List<Ingredient> knownIngredientList = new ArrayList<>();

        if (!ObjectUtils.isEmpty(recipeDtoRequest.getUnKnownIngredients())) {
            // check to see if new ingredients exist
            if (!ingredientRepository.findAllByName(
                    recipeDtoRequest.getUnKnownIngredients().stream().map(String::strip).toList()).isEmpty()) {
                throw new CustomException("Duplicate ingredient name to add new ingredient is not allowed!", HttpStatus.BAD_REQUEST);
            }

            // Save and collect new ingredient
            recipeDtoRequest.getUnKnownIngredients().forEach(newIngredient -> {
                Ingredient ni = ingredientRepository.save(new Ingredient(null, newIngredient.strip(), null));
                knownIngredientList.add(ni);
            });
        }

        // progress known ingredients
        if (!ObjectUtils.isEmpty(recipeDtoRequest.getKnownIngredients())) {
            // prepare unique ingredient ids
            Set<Long> knownIngredientIds = new HashSet<>(recipeDtoRequest.getKnownIngredients());

            List<Ingredient> knownIngredients = ingredientRepository.findAllById(knownIngredientIds.stream().toList());

            // Check to see if it couldn't find ingredient with id
            if (!knownIngredients.isEmpty() && (knownIngredientIds.size() != knownIngredients.size())) {
                throw new CustomException("Couldn't find one or some of known Ingredients!", HttpStatus.BAD_REQUEST);
            }

            knownIngredientList.addAll(knownIngredients);
        }
        return knownIngredientList;
    }

    public RecipeDtoResponse update(RecipeDtoRequest recipeDtoRequest) {
        Optional<Recipe> oldRecipe = recipeRepository.findById(recipeDtoRequest.getId());
        if (oldRecipe.isEmpty()) {
            throw new ResourceNotFoundException("recipe", recipeDtoRequest.getId());
        }

        // keep old ingredient ids to use it later to avoid having not used ingredients
        List<Long> oldRecipeIngredientIds = null;
        if (!ObjectUtils.isEmpty(oldRecipe.get().getIngredients())) {
            oldRecipeIngredientIds = oldRecipe.get().getIngredients().stream().map(Ingredient::getId).toList();
        }
        Recipe result = updateRecipe(recipeDtoRequest);

        List<Long> requiredRecipeIngredientIds = null;
        if (!ObjectUtils.isEmpty(result.getIngredients())) {
            requiredRecipeIngredientIds = result.getIngredients().stream().map(Ingredient::getId).toList();
        }

        removeNotUsedIngredients(oldRecipeIngredientIds, requiredRecipeIngredientIds);

        return RecipeMapper.INSTANCE.toDto(result);
    }

    @Transactional
    private Recipe updateRecipe(RecipeDtoRequest recipeDtoRequest) {
        Recipe recipe = RecipeMapper.INSTANCE.toEntity(recipeDtoRequest);
        recipe.setUserId(getCurrentUser().getId());
        //add required ingredients
        recipe.setIngredients(prepareIngredients(recipeDtoRequest));
        return recipeRepository.save(recipe);
    }

    @Transactional
    private void removeNotUsedIngredients(List<Long> oldRecipeIngredientIds, List<Long> requiredRecipeIngredientIds) {

        if (!ObjectUtils.isEmpty(oldRecipeIngredientIds)) {

            List<Long> notUsedIngredients = new ArrayList<>();

            if (ObjectUtils.isEmpty(requiredRecipeIngredientIds)) {
                notUsedIngredients = oldRecipeIngredientIds;
            } else {
                Map<Comparison, List<Long>> comparisonedNumbers = Utils.comparisonNumbers(
                        List.of(Comparison.EXIST_JUST_IN_LIST1),
                        oldRecipeIngredientIds, requiredRecipeIngredientIds);

                if (!ObjectUtils.isEmpty(comparisonedNumbers)) {
                    // remove ingredients that no need anymore
                    notUsedIngredients = comparisonedNumbers.get(Comparison.EXIST_JUST_IN_LIST1);
                }
            }

            // Remove ingredient in case the ingredient not used for any recipe
            notUsedIngredients.forEach(i -> {
                Optional<Ingredient> ingredient = ingredientRepository.findById(i);
                if (ingredient.isPresent() && ingredient.get().getRecipes().isEmpty()) {
                    // mens this ingredient not used for any recipe and can be removed from ingredients
                    ingredientRepository.deleteById(i);
                }
            });
        }
    }

    public void delete(Long id) {
        Optional<Recipe> oldRecipe = recipeRepository.findById(id);
        if (oldRecipe.isEmpty()) {
            throw new ResourceNotFoundException("recipe", id);
        }

        // keep old ingredient ids to use it later to avoid having not used ingredients
        List<Long> oldRecipeIngredientIds = null;
        if (!ObjectUtils.isEmpty(oldRecipe.get().getIngredients())) {
            oldRecipeIngredientIds = oldRecipe.get().getIngredients().stream().map(Ingredient::getId).toList();
        }

        deleteRecipe(id);

        // Delete ingredients that not be used anymore
        removeNotUsedIngredients(oldRecipeIngredientIds, null);
    }

    @Transactional
    private void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    @Transactional
    public List<RecipeDtoResponse> filterRecipes(Boolean isVegetarian, Integer servings, List<String> includeIngredients,
                                                 List<String> excludeIngredients, String instructions) {
        Specification<Recipe> specification = Specification.where(RecipeSpecification.isVegetarian(isVegetarian))
                .and(RecipeSpecification.hasServings(servings))
                .and(RecipeSpecification.hasInstructions(instructions))
                .and(RecipeSpecification.hasIngredientsIn(includeIngredients))
                .and(RecipeSpecification.doesNotHaveIngredientsIn(excludeIngredients));

        List<Recipe> recipes = recipeRepository.findAll(specification);
        List<RecipeDtoResponse> recipeDtoList = new ArrayList<>();

        recipes.forEach(recipe -> {
            RecipeDtoResponse recipeDto = RecipeMapper.INSTANCE.toDto(recipe);
            recipeDto.setIngredients(IngredientMapper.INSTANCE.toDto(recipe.getIngredients()));
            recipeDtoList.add(recipeDto);
        });

        return recipeDtoList;
    }
}
