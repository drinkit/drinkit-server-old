package guru.drinkit.repository.impl

import guru.drinkit.domain.User
import guru.drinkit.repository.UserRepositoryCustom
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.core.query.Update.update
import java.util.*

/**
 * @author pkolmykov
 */
@Suppress("unused")
class UserRepositoryImpl @Autowired constructor(
        val mongoOperations: MongoOperations
) : UserRepositoryCustom {

    override fun incrementRecipeViews(userId: String, recipeId: Int) {
        mongoOperations.upsert(query(
                where("_id").`is`(userId)),
                Update().inc("recipeStatsMap.$recipeId.views", 1).set("recipeStatsMap.$recipeId.lastViewed", Date()),
                User::class.java);
    }

    override fun changeRecipeLike(userId: String, recipeId: Int, liked: Boolean) {
        mongoOperations.upsert(query(
                where("_id").`is`(userId)),
                Update().set("recipeStatsMap.$recipeId.liked", liked),
                User::class.java);
    }

    override fun updateBarItem(userId: String, barItem: User.BarItem) {
        mongoOperations.updateFirst(
                query(where("_id").`is`(userId).and("barItems.ingredientId").`is`(barItem.ingredientId)),
                update("barItems.$", barItem),
                User::class.java)
    }

    override fun addBarItem(userId: String, barItem: User.BarItem) {
        mongoOperations.updateFirst(
                query(where("_id").`is`(userId)),
                Update().push("barItems", barItem),
                User::class.java)
    }

    override fun removeBarItem(userId: String, ingredientId: Int) {
        mongoOperations.updateFirst(
                query(where("_id").`is`(userId)),
                Update().pull("barItems", where("ingredientId").`is`(ingredientId).criteriaObject),
                User::class.java)
    }
}
