package guru.drinkit.service.impl

import guru.drinkit.annotation.MvcTest
import guru.drinkit.controller.common.AbstractMockMvcTest
import guru.drinkit.domain.Ingredient
import guru.drinkit.domain.Recipe
import guru.drinkit.dto.SuggestedIngredientDto
import guru.drinkit.repository.IngredientRepository
import guru.drinkit.repository.RecipeRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.iterable.Extractor
import org.junit.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author pkolmykov
 */
@MvcTest
class SuggestingServiceImplTest : AbstractMockMvcTest() {

    @Autowired
    private lateinit var suggestingService: SuggestingServiceImpl
    @Autowired
    private lateinit var recipeRepository: RecipeRepository
    @Autowired
    private lateinit var ingredientRepository: IngredientRepository

    @Test
    @Throws(Exception::class)
    fun getBestMatchedIngredients() {
        `when`(recipeRepository.findAll()).thenReturn(listOf(
                createRecipe(1, 1, 2),
                createRecipe(2, 1, 2, 3)
        ))
        `when`(ingredientRepository.findAll()).thenReturn(arrayListOf(Ingredient(1), Ingredient(2), Ingredient(3), Ingredient(4)))


        val matchedRecipes = suggestingService.suggestIngredients(setOf(1, 2))

        println(matchedRecipes)

        assertThat(matchedRecipes)
                .flatExtracting(Extractor<SuggestedIngredientDto, Collection<Int>> { it.recipeIds })
                .contains(2).doesNotContain(1)

    }

    private fun createRecipe(recipeId: Int, vararg ingrIds: Int) =
            Recipe(id = recipeId, ingredientsWithQuantities = ingrIds.map { Recipe.IngredientWithQuantity(it, null) })

}