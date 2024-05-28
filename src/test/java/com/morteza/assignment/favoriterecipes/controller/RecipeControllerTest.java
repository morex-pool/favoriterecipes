package com.morteza.assignment.favoriterecipes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.morteza.assignment.favoriterecipes.dto.RecipeDtoRequest;
import com.morteza.assignment.favoriterecipes.entity.User;
import com.morteza.assignment.favoriterecipes.security.MyUserDetails;
import com.morteza.assignment.favoriterecipes.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class RecipeControllerTest {

    @Mock
    private RecipeService recipeService;

    @InjectMocks
    private RecipeController recipeController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        // Mock authentication
        User user = new User();
        user.setId(1L);
        user.setUsername("user1");
        user.setPassword("user11");
        user.setRoles("ROLE_USER");
        myUserDetails = new MyUserDetails(user);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(myUserDetails, null, myUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private UserDetails myUserDetails = null;

    @Test
    @Order(1)
    void testGetRecipeById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/recipes/5")
                        .with(SecurityMockMvcRequestPostProcessors.user(myUserDetails)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(5));
    }


    @Test
    @Order(2)
    void testGetAllRecipes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(myUserDetails)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Vegetarian Salad"));
    }

    @Test
    @Order(3)
    void testUpdateRecipe() throws Exception {
        RecipeDtoRequest request = new RecipeDtoRequest();
        request.setId(1L);
        request.setName("Updated Recipe 1");
        request.setIsVegetarian(true);
        request.setServings(1);
        request.setInstructions("... oven ...");

        mockMvc.perform(MockMvcRequestBuilders.put("/recipes")
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(myUserDetails)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Recipe 1"));
    }


    @Test
    @Order(4)
    void testCreateRecipe() throws Exception {
        RecipeDtoRequest request = new RecipeDtoRequest();
        request.setId(11L);
        request.setName("Create Recipe 11");
        request.setIsVegetarian(true);
        request.setServings(1);
        request.setInstructions("... oven ...");

        mockMvc.perform(MockMvcRequestBuilders.post("/recipes")
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(myUserDetails)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(11))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Create Recipe 11"));
    }

    @Test
    @Order(5)
    void testDeleteRecipe() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/recipes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(myUserDetails)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/recipes/1")
                        .with(SecurityMockMvcRequestPostProcessors.user(myUserDetails)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @Test
    @Order(6)
    void testFilterRecipes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/recipes/filter")
                        .param("isVegetarian", "false")
                        .param("servings", "4")
                        .param("includeIngredients", "Garlic")
                        .param("instructions", "olive oil")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(myUserDetails)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Grilled Chicken"))
                .andReturn();
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
