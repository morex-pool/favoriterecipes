package com.morteza.assignment.favoriterecipes.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDto {
    private Long id;
    @NotNull(message = "Ingredient name is required!")
    @Pattern(regexp = "^.{4,}$", message = "Ingredient name length should be more than three characters!")
    private String name;
}
