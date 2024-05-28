package com.morteza.assignment.favoriterecipes.controller;

import com.morteza.assignment.favoriterecipes.dto.IngredientDto;
import com.morteza.assignment.favoriterecipes.service.IngredientService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingredient")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping("/{id}")
    public IngredientDto get(@PathVariable Long id) {
        return ingredientService.getById(id);
    }

    @GetMapping
    public List<IngredientDto> getAll() {
        return ingredientService.getAll();
    }


    @PostMapping
    public IngredientDto create(@RequestBody @Valid IngredientDto ingredientDto) {
        return ingredientService.save(ingredientDto);
    }

    @PutMapping
    public IngredientDto update(@RequestBody @Valid IngredientDto ingredientDto) {
        return ingredientService.update(ingredientDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        ingredientService.delete(id);
    }

}

