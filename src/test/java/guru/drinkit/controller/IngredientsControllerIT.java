package guru.drinkit.controller;

import guru.drinkit.domain.Ingredient;
import guru.drinkit.service.IngredientService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class IngredientsControllerIT extends AbstractRestMockMvc {

    @Autowired
    IngredientService ingredientService;

    private static final String RESOURCE_ENDPOINT = "/ingredients";

    @Test
    public void testGetIngredients() throws Exception {
        List<Ingredient> ingredients = ingredientService.getIngredients();
        assertTrue(ingredients.size() > 0);
        mockMvc.perform(get(RESOURCE_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ingredients)));
    }

    @Test
    public void testAddNewIngredient() throws Exception {

    }

    @Test
    public void testEditIngredient() throws Exception {

    }

    @Test
    public void testDelete() throws Exception {
        assertNotNull(ingredientService.getIngredientById(firstIngredient.getId()));
        mockMvc.perform(delete(RESOURCE_ENDPOINT + "/" + firstIngredient.getId()))
                .andExpect(status().isNoContent());
        assertNull(ingredientService.getIngredientById(firstIngredient.getId()));
        mockMvc.perform(delete(RESOURCE_ENDPOINT + "/" + firstIngredient.getId()))
                .andExpect(status().isNotFound());
    }
}