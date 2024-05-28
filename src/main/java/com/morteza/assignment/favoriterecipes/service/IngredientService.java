package com.morteza.assignment.favoriterecipes.service;

import com.morteza.assignment.favoriterecipes.dto.IngredientDto;
import com.morteza.assignment.favoriterecipes.entity.Ingredient;
import com.morteza.assignment.favoriterecipes.exceptions.CustomException;
import com.morteza.assignment.favoriterecipes.exceptions.ResourceNotFoundException;
import com.morteza.assignment.favoriterecipes.mapper.IngredientMapper;
import com.morteza.assignment.favoriterecipes.repository.IngredientRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<IngredientDto> getAll() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        return IngredientMapper.INSTANCE.toDto(ingredients);
    }

    public IngredientDto getById(Long id) {
        Ingredient ingredient = ingredientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ingredient", id));
        return IngredientMapper.INSTANCE.toDto(ingredient);
    }

    @Transactional
    public IngredientDto save(IngredientDto ingredientDto) {
        if (ingredientRepository.findByName(ingredientDto.getName()).isPresent()) {
            throw new CustomException("Duplicate ingredient name is not allowed!", HttpStatus.BAD_REQUEST);
        }
        Ingredient ingredient = IngredientMapper.INSTANCE.toEntity(ingredientDto);
        Ingredient result = ingredientRepository.save(ingredient);
        return IngredientMapper.INSTANCE.toDto(result);
    }

    @Transactional
    public IngredientDto update(IngredientDto ingredientDto) {
        Ingredient ingredient = ingredientRepository.findById(ingredientDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("ingredient", ingredientDto.getId()));
        if (!ingredient.getRecipes().isEmpty()) {
            throw new CustomException("Ingredient is using with other recipes!", HttpStatus.BAD_REQUEST);
        }
        Ingredient result = ingredientRepository.save(IngredientMapper.INSTANCE.toEntity(ingredientDto));
        return IngredientMapper.INSTANCE.toDto(result);
    }

    public void delete(Long id) {
        Ingredient ingredient = ingredientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ingredient", id));
        if (!ingredient.getRecipes().isEmpty()) {
            throw new CustomException("Ingredient is using with other recipes!", HttpStatus.BAD_REQUEST);
        }
        ingredientRepository.deleteById(id);
    }
}
