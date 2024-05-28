package com.morteza.assignment.favoriterecipes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.morteza.assignment.favoriterecipes.dto.IngredientDto;
import com.morteza.assignment.favoriterecipes.entity.User;
import com.morteza.assignment.favoriterecipes.security.MyUserDetails;
import com.morteza.assignment.favoriterecipes.service.IngredientService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class IngredientControllerTest {

    @Mock
    private IngredientService ingredientService;

    @InjectMocks
    private IngredientController ingredientController;

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
        UserDetails myUserDetails = new MyUserDetails(user);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(myUserDetails, null, myUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @Order(1)
    void testGetIngredientById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/ingredient/1")
                        .with(SecurityMockMvcRequestPostProcessors.user("user1").roles("USER")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Potato"));
    }

    @Test
    @Order(2)
    void testGetAllIngredients() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/ingredient")
                        .with(SecurityMockMvcRequestPostProcessors.user("user1").roles("USER")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Potato"));
    }

    @Test
    @Order(3)
    void testUpdateIngredient() throws Exception {
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setId(18L);
        ingredientDto.setName("Ingredient 18");
        when(ingredientService.update(any(IngredientDto.class))).thenReturn(ingredientDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/ingredient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(ingredientDto))
                        .with(SecurityMockMvcRequestPostProcessors.user("user1").roles("USER")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(18))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Ingredient 18"));
    }

    @Test
    @Order(4)
    void testCreateIngredient() throws Exception {
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setName("Ingredient 19");
        when(ingredientService.save(any(IngredientDto.class))).thenReturn(ingredientDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/ingredient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(ingredientDto))
                        .with(SecurityMockMvcRequestPostProcessors.user("user1").roles("USER")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Ingredient 19"));
    }

    @Test
    @Order(5)
    void testDeleteIngredient() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/ingredient/19")
                        .with(SecurityMockMvcRequestPostProcessors.user("user1").roles("USER")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

