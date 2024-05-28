package com.morteza.assignment.favoriterecipes.mapper;

import com.morteza.assignment.favoriterecipes.dto.RecipeDtoRequest;
import com.morteza.assignment.favoriterecipes.dto.RecipeDtoResponse;
import com.morteza.assignment.favoriterecipes.entity.Recipe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RecipeMapper {

    RecipeMapper INSTANCE = Mappers.getMapper(RecipeMapper.class);

    RecipeDtoResponse toDto(Recipe recipe);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "ingredients", ignore = true)
    Recipe toEntity(RecipeDtoRequest recipeDtoRequest);

    RecipeDtoResponse toEntity(Recipe recipe);
}
