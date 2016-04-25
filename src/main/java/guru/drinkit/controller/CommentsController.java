package guru.drinkit.controller;

import java.util.List;

import guru.drinkit.common.DrinkitUtils;
import guru.drinkit.domain.Comment;
import guru.drinkit.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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
        return commentService.findAllByRecipeId(recipeId);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Comment createComment(@PathVariable Integer recipeId, @RequestBody Comment comment) {
        Assert.isNull(comment.getId());
        DrinkitUtils.INSTANCE.assertEqualsIds(recipeId, comment.getRecipeId());
        DrinkitUtils.INSTANCE.logOperation("Creating comment", comment);
        commentService.save(comment);
        return comment;
    }

    //TODO: need to check equality of author and current user
    @RequestMapping(method = RequestMethod.DELETE, value = "/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable String commentId) {
        if (commentService.find(commentId) != null) {
            DrinkitUtils.INSTANCE.logOperation("Deleting comment", commentId);
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
        DrinkitUtils.INSTANCE.assertEqualsIds(recipeId, comment.getRecipeId());
        DrinkitUtils.INSTANCE.logOperation("Updating comment", comment);
        commentService.save(comment);
    }
}
