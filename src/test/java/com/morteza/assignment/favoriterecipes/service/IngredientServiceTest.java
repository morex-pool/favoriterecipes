package com.morteza.assignment.favoriterecipes.service;

import com.morteza.assignment.favoriterecipes.dto.IngredientDto;
import com.morteza.assignment.favoriterecipes.entity.Ingredient;
import com.morteza.assignment.favoriterecipes.entity.Recipe;
import com.morteza.assignment.favoriterecipes.exceptions.CustomException;
import com.morteza.assignment.favoriterecipes.exceptions.ResourceNotFoundException;
import com.morteza.assignment.favoriterecipes.mapper.IngredientMapper;
import com.morteza.assignment.favoriterecipes.repository.IngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class IngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private IngredientMapper ingredientMapper;

    @InjectMocks
    private IngredientService ingredientService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll() {
        // Given
        Ingredient ingredient = new Ingredient();
        List<Ingredient> ingredients = Collections.singletonList(ingredient);
        IngredientDto ingredientDto = new IngredientDto();
        List<IngredientDto> ingredientDtos = Collections.singletonList(ingredientDto);

        when(ingredientRepository.findAll()).thenReturn(ingredients);
        when(ingredientMapper.toDto(ingredients)).thenReturn(ingredientDtos);

        // When
        List<IngredientDto> result = ingredientService.getAll();

        // Then
        assertThat(result).isEqualTo(ingredientDtos);
        verify(ingredientRepository, times(1)).findAll();
        assertTrue(result.size() == ingredients.size());
    }

    @Test
    public void testGetById() {
        // Given
        Long id = 1L;
        Ingredient ingredient = new Ingredient();
        ingredient.setId(id);
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setId(id);

        when(ingredientRepository.findById(id)).thenReturn(Optional.of(ingredient));
        when(ingredientMapper.toDto(ingredient)).thenReturn(ingredientDto);

        // When
        IngredientDto result = ingredientService.getById(id);

        // Then
        assertThat(result).isEqualTo(ingredientDto);
        verify(ingredientRepository, times(1)).findById(id);
        assertTrue(result.getId() == ingredient.getId());
    }

    @Test
    public void testGetByIdNotFound() {
        // Given
        Long id = 1L;

        when(ingredientRepository.findById(id)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> ingredientService.getById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("ingredient");

        verify(ingredientRepository, times(1)).findById(id);
    }

    @Test
    public void testSave() {
        // Given
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setName("Tomato");

        Ingredient ingredient = new Ingredient();
        ingredient.setName("Tomato");

        when(ingredientRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(ingredient);

        // Stubbing the behavior of toEntity() method with a concrete IngredientDto instance
        when(ingredientMapper.toEntity(ingredientDto)).thenReturn(ingredient); // <-- Ensure correct mocking

        when(ingredientMapper.toDto(any(Ingredient.class))).thenReturn(ingredientDto);

        // When
        IngredientDto result = ingredientService.save(ingredientDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(ingredientDto.getName());

        verify(ingredientRepository, times(1)).findByName(anyString());
        verify(ingredientRepository, times(1)).save(any(Ingredient.class));
    }

    @Test
    public void testSaveDuplicateName() {
        // Given
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setName("Tomato");

        when(ingredientRepository.findByName(ingredientDto.getName())).thenReturn(Optional.of(new Ingredient()));

        // When / Then
        CustomException exception = assertThrows(CustomException.class, () -> ingredientService.save(ingredientDto));
        assertThat(exception).hasMessageContaining("Duplicate ingredient name is not allowed!");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);

        verify(ingredientRepository, times(1)).findByName(ingredientDto.getName());
        verify(ingredientRepository, never()).save(any(Ingredient.class));
    }

    @Test
    public void testUpdate() {
        // Given
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setId(1L);
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setRecipes(Collections.emptyList());
        Ingredient updatedIngredient = new Ingredient();
        updatedIngredient.setId(1L);
        updatedIngredient.setName("Tomato");
        IngredientDto updatedIngredientDto = new IngredientDto();
        updatedIngredientDto.setId(1L);
        updatedIngredientDto.setName("Tomato");

        when(ingredientRepository.findById(ingredientDto.getId())).thenReturn(Optional.of(ingredient));
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(updatedIngredient);
        when(ingredientMapper.toDto(updatedIngredient)).thenReturn(updatedIngredientDto);

        // When
        IngredientDto result = ingredientService.update(ingredientDto);

        // Then
        assertThat(result).isEqualTo(updatedIngredientDto);
        verify(ingredientRepository, times(1)).findById(ingredientDto.getId());
        verify(ingredientRepository, times(1)).save(any(Ingredient.class));
    }

    @Test
    public void testUpdateIngredientInUse() {
        // Given
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setId(1L);
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setRecipes(Collections.singletonList(new Recipe()));

        when(ingredientRepository.findById(ingredientDto.getId())).thenReturn(Optional.of(ingredient));

        // When / Then
        CustomException exception = assertThrows(CustomException.class, () -> ingredientService.update(ingredientDto));
        assertThat(exception).hasMessageContaining("Ingredient is using with other recipes!");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);

        verify(ingredientRepository, times(1)).findById(ingredientDto.getId());
        verify(ingredientRepository, never()).save(any(Ingredient.class));
    }

    @Test
    public void testDelete() {
        // Given
        Long id = 1L;
        Ingredient ingredient = new Ingredient();
        ingredient.setId(id);
        ingredient.setRecipes(Collections.emptyList());

        when(ingredientRepository.findById(id)).thenReturn(Optional.of(ingredient));

        // When
        ingredientService.delete(id);

        // Then
        verify(ingredientRepository, times(1)).findById(id);
        verify(ingredientRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteIngredientInUse() {
        // Given
        Long id = 1L;
        Ingredient ingredient = new Ingredient();
        ingredient.setId(id);
        ingredient.setRecipes(Collections.singletonList(new Recipe()));

        when(ingredientRepository.findById(id)).thenReturn(Optional.of(ingredient));

        // When / Then
        CustomException exception = assertThrows(CustomException.class, () -> ingredientService.delete(id));
        assertThat(exception).hasMessageContaining("Ingredient is using with other recipes!");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);

        verify(ingredientRepository, times(1)).findById(id);
        verify(ingredientRepository, never()).deleteById(id);
    }

    @Test
    public void testDeleteNotFound() {
        // Given
        Long id = 1L;

        when(ingredientRepository.findById(id)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> ingredientService.delete(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("ingredient");

        verify(ingredientRepository, times(1)).findById(id);
    }
}
