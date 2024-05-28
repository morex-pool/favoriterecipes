package com.morteza.assignment.favoriterecipes.mapper;

import com.morteza.assignment.favoriterecipes.dto.IngredientDto;
import com.morteza.assignment.favoriterecipes.entity.Ingredient;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface IngredientMapper {
    IngredientMapper INSTANCE = Mappers.getMapper(IngredientMapper.class);

    IngredientDto toDto(Ingredient ingredient);

    Ingredient toEntity(IngredientDto ingredientDto);

    List<IngredientDto> toDto(List<Ingredient> ingredients);
}
