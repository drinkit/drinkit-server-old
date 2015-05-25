package guru.drinkit.repository.impl;

import guru.drinkit.domain.User;
import guru.drinkit.repository.UserBarRepository;
import org.bson.types.ObjectId;
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
    public void updateBarItem(ObjectId userId, User.BarItem barItem) {
        mongoOperations.updateFirst(
                query(where("_id").is(userId).and("barItems.ingredientId").is(barItem.getIngredientId())),
                update("barItems.$", barItem),
                User.class);
    }

    @Override
    public void addBarItem(ObjectId userId, User.BarItem barItem) {
        mongoOperations.updateFirst(
                query(where("_id").is(userId)),
                new Update().push("barItems", barItem),
                User.class);
    }

    @Override
    public void removeBarItem(ObjectId userId, Integer ingredientId) {
        mongoOperations.updateFirst(
                query(where("_id").is(userId)),
                new Update().pull("barItems", where("ingredientId").is(ingredientId).getCriteriaObject()),
                User.class);
    }
}
