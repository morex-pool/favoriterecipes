package com.morteza.assignment.favoriterecipes.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@Entity(name = "Ingredient")
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Ingredient name is required!")
    @Pattern(regexp = "^.{4,}$", message = "Ingredient name length should be more than three characters!")
    private String name;

    @ManyToMany(mappedBy = "ingredients")
    @JsonBackReference
    private List<Recipe> recipes;

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}


