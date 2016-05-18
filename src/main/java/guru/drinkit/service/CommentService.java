package guru.drinkit.service;

import guru.drinkit.domain.Comment;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * Created by Crabar on 27.03.2016.
 */
public interface CommentService {

    List<Comment> getComments();

    List<Comment> getCommentsByRecipeId(int recipeId);

    Comment findById(int id);

    @PreAuthorize("isAuthenticated()")
    Comment save(Comment comment);

    @PreAuthorize("isAuthenticated()")
    void delete(int id);
}
