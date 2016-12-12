package guru.drinkit.service.impl

import guru.drinkit.domain.Recipe
import guru.drinkit.dto.SuggestedIngredientDto
import guru.drinkit.service.IngredientService
import guru.drinkit.service.RecipeService
import guru.drinkit.service.SuggestingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigInteger

/**
 * @author pkolmykov
 */
@Service
class SuggestingServiceImpl
@Autowired
constructor(private val ingredientService: IngredientService,
            private val recipeService: RecipeService) : SuggestingService {

    override fun suggestIngredients(ingredients: Set<Int>, viewAll: Boolean): List<SuggestedIngredientDto> {
        val allRecipesMasks = getRecipesMasks(viewAll)
        val existingRecipes = findMatchedRecipes(ingredients, allRecipesMasks)
        val newRecipeMasks = allRecipesMasks.filter { !existingRecipes.contains(it.key) }
        return populateWithMatchedRecipes(ingredients, newRecipeMasks)
                .filter { it.value.isNotEmpty() }
                .map { SuggestedIngredientDto(it.key, it.value) }
                .sortedByDescending { it.recipeIds.size }
    }

    private fun populateWithMatchedRecipes(ingredients: Set<Int>, recipeMasks: Map<Int?, BigInteger>) =
            ingredientService.findAll()
                    .map { it.id!! }
                    .subtract(ingredients)
                    .associateBy({ it }, { findMatchedRecipes(ingredients.plusElement(it), recipeMasks) })

    //todo caching
    private fun getRecipesMasks(viewAll: Boolean) = recipeService.findAll()
            .filter { viewAll || it.published }
            .associateBy(Recipe::id, { toRecipeMask(it) })

    private fun toRecipeMask(recipe: Recipe) = recipe.ingredientsWithQuantities
            .map(Recipe.IngredientWithQuantity::ingredientId)
            .fold(BigInteger.ZERO, BigInteger::setBit)

    private fun findMatchedRecipes(ingredients: Set<Int>, recipeMasks: Map<Int?, BigInteger>): List<Int> {
        val ingredientsMask = ingredients.fold(BigInteger.ZERO, BigInteger::setBit)
        return recipeMasks
                .filter { it.value.and(ingredientsMask) == it.value }
                .map { it -> it.key!! }
    }


}
