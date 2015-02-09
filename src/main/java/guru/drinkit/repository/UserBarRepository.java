package guru.drinkit.repository;

import guru.drinkit.domain.User;
import org.bson.types.ObjectId;

/**
 * @author pkolmykov
 */
public interface UserBarRepository {
    void updateBarItem(ObjectId userId, User.BarItem barItem);
}