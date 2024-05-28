package com.morteza.assignment.favoriterecipes.controller;

import com.morteza.assignment.favoriterecipes.dto.RecipeDtoRequest;
import com.morteza.assignment.favoriterecipes.dto.RecipeDtoResponse;
import com.morteza.assignment.favoriterecipes.service.RecipeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/{id}")
    public RecipeDtoResponse get(@PathVariable Long id) {
        return recipeService.getById(id);
    }

    @GetMapping
    public List<RecipeDtoResponse> getAll() {
        return recipeService.getAllRecipes();
    }

    @PostMapping
    public RecipeDtoResponse create(@RequestBody @Valid RecipeDtoRequest recipeDtoRequest) {
        return recipeService.save(recipeDtoRequest);
    }

    @PutMapping
    public RecipeDtoResponse update(@RequestBody @Valid RecipeDtoRequest recipeDtoRequest) {
        return recipeService.update(recipeDtoRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        recipeService.delete(id);
    }

    @GetMapping("/filter")
    public List<RecipeDtoResponse> filter(@RequestParam(required = false) Boolean isVegetarian,
                                          @RequestParam(required = false) Integer servings,
                                          @RequestParam(required = false) List<String> includeIngredients,
                                          @RequestParam(required = false) List<String> excludeIngredients,
                                          @RequestParam(required = false) String instructions) {
        return recipeService.filterRecipes(isVegetarian, servings, includeIngredients, excludeIngredients, instructions);
    }
}

