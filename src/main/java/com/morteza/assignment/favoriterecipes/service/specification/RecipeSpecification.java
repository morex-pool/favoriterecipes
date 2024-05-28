package com.morteza.assignment.favoriterecipes.service.specification;


import com.morteza.assignment.favoriterecipes.entity.Ingredient;
import com.morteza.assignment.favoriterecipes.entity.Recipe;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class RecipeSpecification {

    public static Specification<Recipe> isVegetarian(Boolean isVegetarian) {
        return (root, query, builder) ->
                isVegetarian == null ? null : builder.equal(root.get("isVegetarian"), isVegetarian);
    }

    public static Specification<Recipe> hasServings(Integer servings) {
        return (root, query, builder) ->
                servings == null ? null : builder.equal(root.get("servings"), servings);
    }

    public static Specification<Recipe> hasInstructions(String instructions) {
        return (root, query, builder) ->
                instructions == null ? null : builder.like(root.get("instructions"), "%" + instructions + "%");
    }

    // This method is commented to avoid be used based on the requirements
    /*
    public static Specification<Recipe> belongsToUser(Long userId) {
        return (root, query, builder) ->
                userId == null ? null : builder.equal(root.get("userId"), userId);
    }
    */
    public static Specification<Recipe> hasIngredientsIn(List<String> includeIngredients) {
        return (root, query, builder) -> {
            if (includeIngredients == null || includeIngredients.isEmpty()) {
                return null;
            }
            Join<Object, Object> ingredients = root.join("ingredients", JoinType.INNER);
            return ingredients.get("name").in(includeIngredients);
        };
    }

    public static Specification<Recipe> doesNotHaveIngredientsIn(List<String> excludeIngredients) {
        return (root, query, criteriaBuilder) -> {
            if (excludeIngredients == null || excludeIngredients.isEmpty()) {
                return null;
            }

            // Create subquery
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Recipe> subRoot = subquery.from(Recipe.class);
            Join<Recipe, Ingredient> subIngredientsJoin = subRoot.join("ingredients");
            subquery.select(subRoot.get("id"))
                    .where(subIngredientsJoin.get("name").in(excludeIngredients));

            // Predicate to exclude recipes
            return criteriaBuilder.not(root.get("id").in(subquery));
        };
    }

}
