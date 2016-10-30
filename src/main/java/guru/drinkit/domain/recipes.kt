package guru.drinkit.domain

import org.springframework.data.annotation.PersistenceConstructor
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

/**
 * @author pkolmykov
 */
@Document
data class Recipe(
        override var id: Int? = null,
        var cocktailTypeId: Int = 0,
        var description: String? = null,
        var name: String? = null,
        var options: List<Int>? = null,
        var ingredientsWithQuantities: List<IngredientWithQuantity> = emptyList(),
        var imageUrl: String? = null,
        var thumbnailUrl: String? = null,
        var createdDate: Date? = Date(),
        var addedBy: String? = null,
        var published: Boolean = false,
        var stats: Stats? = null) : Entity<Int> {
    data class Stats(val likes: Int = 0, val views: Int = 0)
    data class IngredientWithQuantity
    @PersistenceConstructor constructor(
            val ingredientId: Int,
            val quantity: Int? = null,
            val unit: String? = null)
}

@Document
data class Ingredient(
        override var id: Int? = null,
        @Indexed(unique = true)
        var name: String? = null,
        var vol: Int? = null,
        var description: String? = null,
        var category: String? = null,
        var alias: List<String>? = null) : Entity<Int>
