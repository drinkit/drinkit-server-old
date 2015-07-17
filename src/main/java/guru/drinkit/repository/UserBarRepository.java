package guru.drinkit.repository;

import guru.drinkit.domain.BarItem;

import java.lang.String;

/**
 * @author pkolmykov
 */
public interface UserBarRepository {
    void updateBarItem(String userId, BarItem barItem);

    void addBarItem(String userId, BarItem barItem);

    void removeBarItem(String userId, Integer ingredientId);
}
