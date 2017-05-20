package guru.drinkit.controller

import guru.drinkit.controller.RecipeController.Companion.RESOURCE_NAME
import guru.drinkit.controller.common.AbstractMockMvcTest
import guru.drinkit.domain.Recipe
import guru.drinkit.repository.RecipeRepository
import guru.drinkit.security.Role
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * @author pkolmykov
 */
@Suppress("SpringKotlinAutowiredMembers")
class RecipeControllerTest : AbstractMockMvcTest() {

    @Autowired
    private lateinit var recipeRepository: RecipeRepository

    @Before
    fun setUp() {
        `when`(recipeRepository.findOne(1)).thenReturn(Recipe(name = ""))
        `when`(recipeRepository.save(anyObject<Recipe>())).thenReturn(Recipe(id = 1, name = ""))
    }

    @Test
    fun getRecipeById() {
        verifyAccess({ MockMvcRequestBuilders.get(RESOURCE_NAME + "/1") }, status().isOk)
    }

    @Test
    fun createRecipe() {
        val recipe = objectMapper.writeValueAsBytes(Recipe(ingredientsWithQuantities = listOf(Recipe.IngredientWithQuantity(1, 11, null)), name = ""))
        verifyAccess({ post(RESOURCE_NAME).content((recipe)).contentType(APPLICATION_JSON) },
                status().isCreated, Role.ADMIN)
    }

    @Test
    fun searchRecipes() {
        verifyAccess({
            MockMvcRequestBuilders.get(RESOURCE_NAME + "?criteria=null")
        }, status().isOk)
    }

    @Test
    fun deleteRecipe() {
        verifyAccess({ MockMvcRequestBuilders.delete(RESOURCE_NAME + "/1") },
                status().isNoContent, Role.ADMIN)
    }

    @Test
    fun updateRecipe() {
        val recipe = objectMapper.writeValueAsString(Recipe(id = 1, name = ""))
        verifyAccess({ put(RESOURCE_NAME + "/1").content(recipe).contentType(APPLICATION_JSON) }, status().isNoContent, Role.ADMIN)
    }

    @Test
    fun findRecipesByNamePart() {
        verifyAccess({
            MockMvcRequestBuilders.get(RESOURCE_NAME + "/1")
        }, status().isOk)
    }

    @Test
    fun uploadMedia() {
        verifyAccess({
            post(RESOURCE_NAME + "/1/media")
                    .content("{}")
                    .contentType(APPLICATION_JSON)
        }, status().isBadRequest, Role.ADMIN)
    }

}