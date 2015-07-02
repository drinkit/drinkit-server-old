package guru.drinkit.repository;

import guru.drinkit.domain.User;
import java.lang.String;

/**
 * @author pkolmykov
 */
public interface UserBarRepository {
    void updateBarItem(String userId, User.BarItem barItem);

    void addBarItem(String userId, User.BarItem barItem);

    void removeBarItem(String userId, Integer ingredientId);
}
