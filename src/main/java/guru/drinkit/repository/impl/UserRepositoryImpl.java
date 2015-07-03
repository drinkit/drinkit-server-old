package guru.drinkit.repository.impl;

import guru.drinkit.domain.BarItem;
import guru.drinkit.domain.User;
import guru.drinkit.repository.UserBarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

/**
 * @author pkolmykov
 */
@SuppressWarnings({"SpringJavaAutowiredMembersInspection", "UnusedDeclaration"})
public class UserRepositoryImpl implements UserBarRepository {
    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public void updateBarItem(String userId, BarItem barItem) {
        mongoOperations.updateFirst(
                query(where("_id").is(userId).and("barItems.ingredientId").is(barItem.getIngredientId())),
                update("barItems.$", barItem),
                User.class);
    }

    @Override
    public void addBarItem(String userId, BarItem barItem) {
        mongoOperations.updateFirst(
                query(where("_id").is(userId)),
                new Update().push("barItems", barItem),
                User.class);
    }

    @Override
    public void removeBarItem(String userId, Integer ingredientId) {
        mongoOperations.updateFirst(
                query(where("_id").is(userId)),
                new Update().pull("barItems", where("ingredientId").is(ingredientId).getCriteriaObject()),
                User.class);
    }
}
