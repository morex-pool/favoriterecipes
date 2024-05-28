package com.morteza.assignment.favoriterecipes.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Entity(name = "Recipe")
@NoArgsConstructor
@AllArgsConstructor
public class Recipe implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Recipe name is required!")
    @Pattern(regexp = "^.{4,}$", message = "Recipe name length should be more than three characters!")
    private String name;

    @NotNull(message = "Recipe isVegetarian is required!")
    private Boolean isVegetarian;

    @NotNull(message = "Recipe servings is required!")
    private Integer servings;

    @NotNull(message = "Recipe instructions is required!")
    private String instructions;

    @NotNull(message = "Recipe userId is required!")
    private Long userId;

    @ManyToMany
    @JoinTable(
            name = "recipe_ingredient",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    @JsonManagedReference
    private List<Ingredient> ingredients;

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isVegetarian=" + isVegetarian +
                ", servings=" + servings +
                ", instructions='" + instructions + '\'' +
                ", userId=" + userId +
                '}';
    }
}

