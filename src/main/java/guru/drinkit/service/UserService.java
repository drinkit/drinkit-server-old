package guru.drinkit.service;

import guru.drinkit.domain.User;

import java.util.List;

/**
 * Created by Crabar on 20.03.2016.
 */
public interface UserService {
    void updateLikes(User user, List<Integer> likes);
}
