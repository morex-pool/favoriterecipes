package com.morteza.assignment.favoriterecipes.repository;

import com.morteza.assignment.favoriterecipes.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    Optional<Ingredient> findByName(String name);

    @Query("SELECT i FROM Ingredient i WHERE i.name IN :names")
    List<Ingredient> findAllByName(@Param("names") List<String> names);
}

