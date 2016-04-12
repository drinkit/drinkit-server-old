package guru.drinkit.controller.it;

import guru.drinkit.controller.common.AbstractRestMockMvc;
import guru.drinkit.domain.Ingredient;
import guru.drinkit.domain.Recipe;
import guru.drinkit.service.IngredientService;
import guru.drinkit.service.RecipeService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author pkolmykov
 */
public class RestEndpointsITCase extends AbstractRestMockMvc {

    @Autowired
    private RecipeService recipeService;
    @Autowired
    private IngredientService ingredientService;


    @Test
    public void testDeleteRecipeShouldReturn409() throws Exception {
        int ingredientId = ingredientService.save(createNewIngredient()).getId();
        Recipe newRecipe = createNewRecipeDto();
        newRecipe.setIngredientsWithQuantities(singletonList(new Recipe.IngredientWithQuantity(ingredientId, 100)));
        recipeService.insert(newRecipe);
        mockMvc.perform(delete("/ingredients/" + ingredientId))
                .andExpect(status().isConflict());
        assertNotNull(ingredientService.getIngredientById(ingredientId));
    }

    private Ingredient createNewIngredient() {
        Ingredient ingredient = new Ingredient();
        ingredient.setDescription("new ingredient");
        ingredient.setName("new");
        ingredient.setVol(44);
        return ingredient;
    }

}
