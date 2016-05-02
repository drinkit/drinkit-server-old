package guru.drinkit.controller.it;

import java.util.List;

import guru.drinkit.controller.common.AbstractRestMockMvc;
import guru.drinkit.domain.Ingredient;
import guru.drinkit.repository.UserRepository;
import guru.drinkit.service.IngredientService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class IngredientsControllerIT extends AbstractRestMockMvc {

    private static final String RESOURCE_ENDPOINT = "/ingredients";
    @Autowired
    private IngredientService ingredientService;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testGetIngredients() throws Exception {
        List<Ingredient> ingredients = ingredientService.findAll();
        assertTrue(ingredients.size() > 0);
        mockMvc.perform(get(RESOURCE_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ingredients)));
    }

    @Test(expected = DuplicateKeyException.class)//todo test through controller
    public void testAddNewIngredientWithDuplicatedName() throws Exception {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(firstIngredient.getName());
        ingredientService.insert(ingredient);
    }

    @Test
    public void testEditIngredient() throws Exception {

    }

    @Test
    public void testDelete() throws Exception {
        Integer ingredientId = firstIngredient.getId();
        assertNotNull(ingredientService.find(ingredientId));
        assertThat(userRepository.findByUserBarIngredientId(ingredientId).size()).isGreaterThan(0);
        mockMvc.perform(delete(RESOURCE_ENDPOINT + "/" + ingredientId))
                .andExpect(status().isNoContent());

        assertThat(ingredientService.find(ingredientId)).isNull();
        assertThat(userRepository.findByUserBarIngredientId(ingredientId).size()).isEqualTo(0);

        assertNull(ingredientService.find(ingredientId));
        mockMvc.perform(delete(RESOURCE_ENDPOINT + "/" + ingredientId))
                .andExpect(status().isNotFound());
    }
}