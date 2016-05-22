package guru.drinkit.controller

import guru.drinkit.controller.RecipeController.Companion.RESOURCE_NAME
import guru.drinkit.controller.common.AbstractMockMvcTest
import guru.drinkit.domain.Recipe
import guru.drinkit.repository.RecipeRepository
import guru.drinkit.security.Role
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.util.NestedServletException

/**
 * @author pkolmykov
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
class RecipeControllerTest : AbstractMockMvcTest() {

    @Autowired
    private lateinit var recipeRepository: RecipeRepository

    @Before
    fun setUp() {
        `when`(recipeRepository.findOne(1)).thenReturn(Recipe())
        `when`(recipeRepository.save(anyObject<Recipe>())).thenReturn(Recipe(id = 1))
    }

    fun <T> anyObject(): T {
        Mockito.anyObject<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T


    @Test
    fun getRecipeById() {
        verifyAccess(httpMethod = GET, uri = RESOURCE_NAME + "/1", resultMatcher = status().isOk)
    }

    @Test
    fun createRecipe() {
        val recipe = Recipe()
        recipe.ingredientsWithQuantities = listOf(Recipe.IngredientWithQuantity(1, 11))
        verifyAccess(POST, RESOURCE_NAME, recipe, status().isCreated, Role.ADMIN)
    }

    @Test
    fun searchRecipes() {
        verifyAccess(httpMethod = GET, uri = RESOURCE_NAME + "?criteria=null", resultMatcher = status().isOk)
    }

    @Test
    fun deleteRecipe() {
        verifyAccess(httpMethod = DELETE, uri = RESOURCE_NAME + "/1", resultMatcher = status().isNoContent, allowed = Role.ADMIN)
    }

    @Test
    fun updateRecipe() {
        val recipe = Recipe()
        recipe.id = 1
        verifyAccess(httpMethod = PUT, uri = RESOURCE_NAME + "/1", body = recipe, resultMatcher = status().isNoContent, allowed = Role.ADMIN)
    }

    @Test
    fun findRecipesByNamePart() {
        verifyAccess(httpMethod = GET, uri = RESOURCE_NAME + "/1", resultMatcher = status().isOk)
    }

    @Test
    fun uploadMedia() {
        try {
            verifyAccess(httpMethod = POST, uri = RESOURCE_NAME + "/1/media", resultMatcher = status().isNoContent, allowed = Role.ADMIN)
        } catch(e: NestedServletException) {
            Assertions.assertThat(e.cause).isInstanceOf(IllegalArgumentException::class.java)
            Assertions.assertThat(e.cause?.message).isEqualTo("image AND thumbnail is required")
        }

    }

}