package guru.drinkit.domain

import org.bson.types.ObjectId
import org.springframework.data.annotation.PersistenceConstructor
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

/**
 * @author pkolmykov
 */
data class User(

        var id: String? = null,
        var username: String? = null,
        var password: String? = null,
        var displayName: String? = null,
        var accessLevel: Int? = null,
        var barItems: List<BarItem>? = null,
        var likes: List<Int>? = null) {

    data class BarItem(var ingredientId: Int? = null, var isActive: Boolean = true)

    companion object {
        const val ACCESS_LVL_USER = 9
        const val ACCESS_LVL_ADMIN = 0
    }

}

@Document
data class Comment
@PersistenceConstructor constructor(
        override val id: String?,
        @Indexed val recipeId: Int,
        var posted: Date,
        var author: Author,
        val text: String) : Entity<String> {
    data class Author(val userId: String, val name: String)
}

data class UserRecipeStats(
        val id: ObjectId? = null,
        val userId: String,
        val recipeId: Int,
        val views: Int,
        val lastViewed: Date) {

}

