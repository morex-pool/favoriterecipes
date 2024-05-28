package com.morteza.assignment.favoriterecipes.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDtoRequest {
    private Long id;
    @NotNull(message = "Recipe's name is required!")
    @Pattern(regexp = "^.{4,}$", message = "Recipe name length should be more than three characters!")
    private String name;
    @NotNull(message = "Recipe's isVegetarian is required!")
    private Boolean isVegetarian;
    @NotNull(message = "Recipe's servings is required!")
    private Integer servings;
    @NotNull(message = "Recipe's instructions is required!")
    private String instructions;
    List<Long> knownIngredients;
    List<String> unKnownIngredients;
}
