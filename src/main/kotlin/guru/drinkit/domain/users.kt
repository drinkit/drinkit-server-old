package guru.drinkit.domain

import org.bson.types.ObjectId
import org.springframework.data.annotation.PersistenceConstructor
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


    data class BarItem(var ingredientId: Int? = null, var isActive: Boolean = true) {
        constructor(ingredientId: Int?) : this(ingredientId, isActive = true)
    }

    companion object {
        const val ACCESS_LVL_USER = 9
        const val ACCESS_LVL_ADMIN = 0
    }

}

data class Comment(
        var id: Int? = null,
        val recipeId: Int,
        val posted: Date,
        val author: CommentAuthor,
        val text: String) {
    data class CommentAuthor(var id: String? = null, val name: String)
}

data class UserRecipeStats(
        val id: ObjectId? = null,
        val userId: String,
        val recipeId: Int,
        val views: Int,
        val lastViewed: Date) {

    constructor(recipeId: Int, userId: String)
    : this(userId, recipeId, 1, Date())

    @PersistenceConstructor
    constructor(userId: String, recipeId: Int, views: Int, lastViewed: Date)
    : this(null, userId, recipeId, views, lastViewed)
}

