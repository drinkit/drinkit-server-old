package guru.drinkit.controller

import guru.drinkit.annotation.MvcTest
import guru.drinkit.controller.IngredientController.Companion.RESOURCE_NAME
import guru.drinkit.controller.common.AbstractMockMvcTest
import guru.drinkit.domain.Ingredient
import guru.drinkit.domain.Recipe
import guru.drinkit.repository.IngredientRepository
import guru.drinkit.repository.RecipeRepository
import guru.drinkit.security.Role
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * @author pkolmykov
 */

@MvcTest
class IngredientControllerTest : AbstractMockMvcTest() {

    @Autowired
    private lateinit var ingredientRepository: IngredientRepository
    @Autowired
    private lateinit var recipeRepository: RecipeRepository

    @Before
    fun setUp() {
        `when`(ingredientRepository.findAll()).thenReturn(listOf(Ingredient(1), Ingredient(2), Ingredient(3)))
        `when`(ingredientRepository.findOne(1)).thenReturn(Ingredient(id = 1))
        `when`(ingredientRepository.save(anyObject<Ingredient>())).thenReturn(Ingredient(id = 1))
    }

    @Test
    fun getIngredients() {
        verifyAccess({ get(RESOURCE_NAME) }, status().isOk)
    }

    @Test
    fun getIngredientById() {
        verifyAccess({ MockMvcRequestBuilders.get(RESOURCE_NAME + "/1") }, status().isOk)
    }

    @Test
    fun addNewIngredient() {
        val ingredientJson = objectMapper.writeValueAsBytes(Ingredient(name = "ingr", description = "any", vol = 40, category = "cat"))
        verifyAccess({ MockMvcRequestBuilders.post(RESOURCE_NAME).content((ingredientJson)).contentType(MediaType.APPLICATION_JSON) },
                status().isCreated, Role.ADMIN)
    }

    @Test
    fun editIngredient() {
        val ingredientJson = objectMapper.writeValueAsBytes(Ingredient(id = 1, name = "ingr", description = "any", vol = 40, category = "cat"))
        verifyAccess({ MockMvcRequestBuilders.put(RESOURCE_NAME + "/1").content(ingredientJson).contentType(MediaType.APPLICATION_JSON) }, status().isNoContent, Role.ADMIN)
    }

    @Test
    fun delete() {
        verifyAccess({ MockMvcRequestBuilders.delete(RESOURCE_NAME + "/1") },
                status().isNoContent, Role.ADMIN)
    }

    @Test
    fun suggest() {
        `when`(recipeRepository.findAll()).thenReturn(listOf(createRecipe(1, 3)))
        verifyAccess({
            get(RESOURCE_NAME + "/suggest")
                    .param("id[]", "1")
                    .param("id[]", "2")
                    .param("id[]", "3")
                    .param("viewAll", "true")
        }, status().isOk)

    }

    private fun createRecipe(recipeId: Int, vararg ingrIds: Int) =
            Recipe(id = recipeId, ingredientsWithQuantities = ingrIds.map { Recipe.IngredientWithQuantity(it, null, null) })
}