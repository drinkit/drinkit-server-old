package guru.drinkit.domain

import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

/**
 * @author pkolmykov
 */
@Document
data class User(
        var id: String? = null,
        @Indexed(unique = true) var username: String = ANONYMOUS_USER_NAME,
        var password: String = "",
        var displayName: String = "",
        var accessLevel: Int = ACCESS_LVL_USER,
        var barItems: List<BarItem> = emptyList(),
        var recipeStatsMap: Map<Int, RecipeStats> = emptyMap()) {

    data class BarItem(val ingredientId: Int, var active: Boolean)

    data class RecipeStats(
            val views: Int,
            val lastViewed: Date,
            val liked: Boolean?)

    companion object {
        const val ACCESS_LVL_USER = 9
        const val ACCESS_LVL_ADMIN = 0
        const val ANONYMOUS_USER_NAME = "ANONYMOUS"
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




