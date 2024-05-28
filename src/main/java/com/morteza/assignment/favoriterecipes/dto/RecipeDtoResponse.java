package com.morteza.assignment.favoriterecipes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDtoResponse {
    private Long id;
    private String name;
    private Boolean isVegetarian;
    private Integer servings;
    private String instructions;
    List<IngredientDto> ingredients;
}
