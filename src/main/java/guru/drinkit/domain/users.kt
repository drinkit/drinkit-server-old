package guru.drinkit.domain

import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

/**
 * @author pkolmykov
 */
data class User(
        var id: String?,
        var username: String,
        var password: String,
        var displayName: String,
        val accessLevel: Int = ACCESS_LVL_USER,
        val barItems: List<BarItem> = emptyList(),
        val recipesStats: Map<Int, RecipeStats> = emptyMap()) {

    data class BarItem(val ingredientId: Int, val isActive: Boolean)

    data class RecipeStats(
            val views: Int,
            val lastViewed: Date,
            val liked: Boolean)

    companion object {
        const val ACCESS_LVL_USER = 9
        const val ACCESS_LVL_ADMIN = 0
    }

}

@Document
data class Comment(
        override val id: String?,
        @Indexed val recipeId: Int,
        var posted: Date,
        var author: Author,
        val text: String) : Entity<String> {
    data class Author(val userId: String, val name: String)
}




