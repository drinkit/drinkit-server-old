package guru.drinkit.service.impl

import guru.drinkit.domain.Comment
import guru.drinkit.domain.User
import guru.drinkit.repository.CommentRepository
import guru.drinkit.service.CommentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

/**
 * Created by Crabar on 27.03.2016.
 */
@Service
open class CommentServiceImpl @Autowired constructor(
        private val commentRepository: CommentRepository
) : CommentService, CrudServiceImpl<String, Comment>(commentRepository) {

    override fun findAllByRecipeId(recipeId: Int): List<Comment> = commentRepository.findAllByRecipeId(recipeId)


    fun insert(entity: Comment, user: User): Comment {
        entity.posted = Date()
        entity.author = Comment.Author(user.username, user.displayName)
        return super<CrudServiceImpl>.insert(entity)
    }
}
