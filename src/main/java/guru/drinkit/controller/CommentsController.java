package guru.drinkit.controller;

import guru.drinkit.common.DrinkitUtils;
import guru.drinkit.domain.Comment;
import guru.drinkit.domain.User;
import guru.drinkit.repository.UserRepository;
import guru.drinkit.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Crabar on 27.03.2016.
 */
@Controller
@RequestMapping("recipes/{recipeId}/comments")
public class CommentsController {

    @Autowired
    private CommentService commentService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Comment> getRecipeComments(@PathVariable Integer recipeId) {
        return commentService.getCommentsByRecipeId(recipeId);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Comment createComment(@PathVariable Integer recipeId, @RequestBody Comment comment) {
        Assert.isNull(comment.getId());
        DrinkitUtils.assertEqualsIds(recipeId, comment.getRecipeId());
        DrinkitUtils.logOperation("Creating comment", comment);
        commentService.save(comment);
        return comment;
    }

    //TODO: need to check equality of author and current user
    @RequestMapping(method = RequestMethod.DELETE, value = "/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer commentId) {
        if (commentService.findById(commentId) != null) {
            DrinkitUtils.logOperation("Deleting comment", commentId);
            commentService.delete(commentId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //TODO: need to check equality of author and current user
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateContent(@PathVariable Integer recipeId, @RequestBody Comment comment) {
        DrinkitUtils.assertEqualsIds(recipeId, comment.getRecipeId());
        DrinkitUtils.logOperation("Updating comment", comment);
        commentService.save(comment);
    }
}
