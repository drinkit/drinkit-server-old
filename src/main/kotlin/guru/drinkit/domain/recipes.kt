package guru.drinkit.domain

import java.util.*

/**
 * @author pkolmykov
 */
data class Recipe(
        var id: Int? = null,
        var cocktailTypeId: Int = 0,
        var description: String? = null,
        var name: String? = null,
        var options: IntArray? = null,
        var ingredientsWithQuantities: List<IngredientWithQuantity>? = null,
        var imageUrl: String? = null,
        var thumbnailUrl: String? = null,
        var createdDate: Date? = Date(),
        var addedBy: String? = null,
        var published: Boolean = false,
        var likes: Int? = null,
        var views: Int? = null) {
    data class IngredientWithQuantity(val ingredientId: Int, val quantity: Int)
}
