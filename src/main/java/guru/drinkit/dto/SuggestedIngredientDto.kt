package guru.drinkit.dto

/**
 * @author pkolmykov
 */
data class SuggestedIngredientDto(
        val ingredientId: Int,
        val recipeIds: List<Int>
)

