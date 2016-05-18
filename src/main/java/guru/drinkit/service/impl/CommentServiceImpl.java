package guru.drinkit.service.impl;

import java.util.List;

import guru.drinkit.domain.Comment;
import guru.drinkit.exception.RecordNotFoundException;
import guru.drinkit.repository.CommentRepository;
import guru.drinkit.repository.UserRepository;
import guru.drinkit.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Crabar on 27.03.2016.
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Comment> getComments() {
        return commentRepository.findAll();
    }

    @Override
    public List<Comment> getCommentsByRecipeId(int recipeId) {
        return commentRepository.getCommentsByRecipeId(recipeId);
    }

    @Override
    public Comment findById(int id) {
        return commentRepository.findOne(id);
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == null) {
            Comment lastIngredient = commentRepository.findFirstByOrderByIdDesc();
            comment.setId(lastIngredient == null ? 1 : lastIngredient.getId() + 1);
        }
        return commentRepository.save(comment);
    }

    @Override
    public void delete(int id) {
        Comment comment = commentRepository.findOne(id);
        if (comment == null) {
            throw new RecordNotFoundException("Comment not found : " + id);
        }

        commentRepository.delete(comment);
    }
}
