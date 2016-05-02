package guru.drinkit.repository.impl

import guru.drinkit.domain.Recipe
import guru.drinkit.repository.RecipeRepositoryCustom
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.Update

@Suppress("unused")
/**
 * Created by pkolmykov on 12/8/2014.
 */
class RecipeRepositoryImpl @Autowired constructor(
        private val operations: MongoOperations
) : RecipeRepositoryCustom {


    override fun findByCriteria(criteria: guru.drinkit.service.Criteria): List<Recipe> {
        val mongoCriteria = Criteria()
        if (criteria.ingredients.size > 0) {
            mongoCriteria.and("ingredientsWithQuantities").elemMatch(where("ingredientId").`in`(criteria.ingredients))
        }
        if (criteria.options.size > 0) {
            mongoCriteria.and("options").all(criteria.options)
        }
        if (criteria.cocktailTypes.size > 0) {
            mongoCriteria.and("cocktailTypeId").`in`(criteria.cocktailTypes)
        }

        return operations.find(query(mongoCriteria), Recipe::class.java)
    }


    override fun adjustViewsCount(recipeId: Int, shiftValue: Int) {
        operations.findAndModify(query(where("id").`is`(recipeId)), Update().inc("stats.views", 1), Recipe::class.java);
    }

    override  fun adjustLikesCount(recipeId: Int, shiftValue: Int) {
        operations.findAndModify(query(where("id").`is`(recipeId)), Update().inc("stats.likes", shiftValue), Recipe::class.java)
    }
}
