package guru.drinkit.repository;

import guru.drinkit.domain.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created by Crabar on 27.03.2016.
 */
public interface CommentRepository extends MongoRepository<Comment, Integer> {
    Comment findFirstByOrderByIdDesc();

    @Query("{'recipeId' : ?0}")
    List<Comment> getCommentsByRecipeId(int recipeId);
}
