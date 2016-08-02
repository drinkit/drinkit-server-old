package guru.drinkit.controller

import guru.drinkit.domain.Comment
import guru.drinkit.service.CommentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

/**
 * Created by Crabar on 27.03.2016.
 */
@Controller
@RequestMapping("recipes/{recipeId}/comments")
open class CommentsController @Autowired constructor(
        val commentService: CommentService
) {

    @RequestMapping(method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun getRecipeComments(@PathVariable recipeId: Int): List<Comment> {
        return commentService.findAllByRecipeId(recipeId)
    }

    @RequestMapping(method = arrayOf(RequestMethod.POST))
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    fun createComment(@RequestBody comment: Comment): Comment {
        commentService.insert(comment)
        return comment
    }

    //TODO: need to check equality of author and current user
    @RequestMapping(method = arrayOf(RequestMethod.DELETE), value = "/{commentId}")
    fun deleteComment(@PathVariable commentId: String): ResponseEntity<Void> {
        if (commentService.find(commentId) != null) {
            commentService.delete(commentId)
            return ResponseEntity.noContent().build()
        } else {
            return ResponseEntity.notFound().build()
        }
    }

    //TODO: need to check equality of author and current user
    @RequestMapping(method = arrayOf(RequestMethod.PUT))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateContent(@PathVariable recipeId: Int, @RequestBody comment: Comment) {
        commentService.update(recipeId, comment)
    }
}
