package guru.drinkit.service.impl;

import java.util.List;
import javax.annotation.Resource;

import guru.drinkit.domain.User;
import guru.drinkit.repository.RecipeRepository;
import guru.drinkit.repository.UserRepository;
import guru.drinkit.service.UserService;
import org.springframework.stereotype.Service;

/**
 * Created by Crabar on 20.03.2016.
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private RecipeRepository recipeRepository;

    @Override
    public void updateLikes(User user, List<Integer> likes) {
//        if (user.getLikes() != null) {
//            for (Integer recipeId : user.getLikes()) {
//                final boolean recipeWasDisliked = !likes.contains(recipeId);
//                if (recipeWasDisliked) {
//                    recipeRepository.decrementLikes(recipeId);
//                }
//            }
//        }
//
//        for (Integer recipeId : likes) {
//            final boolean recipeWasLiked = user.getLikes() == null || !user.getLikes().contains(recipeId);
//
//            if (recipeWasLiked) {
//                recipeRepository.incrementLikes(recipeId);
//            }
//
//        }
//
//        user.setLikes(likes);
//        userRepository.save(user);
    }
}
