package guru.drinkit.service

import guru.drinkit.domain.Comment

/**
 * Created by Crabar on 27.03.2016.
 */
interface CommentService : CrudService<String, Comment> {

    fun findAllByRecipeId(recipeId: Int): List<Comment>

    override fun delete(entity: Comment)


}
