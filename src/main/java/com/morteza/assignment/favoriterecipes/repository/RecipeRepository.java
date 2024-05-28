package com.morteza.assignment.favoriterecipes.repository;

import com.morteza.assignment.favoriterecipes.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {
    List<Recipe> findByUserId(Long id);

    Optional<Object> findByNameAndUserId(String name, Long userId);
}

