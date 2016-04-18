package guru.drinkit.repository

import guru.drinkit.common.Criteria
import guru.drinkit.domain.*
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.Repository

/**
 * @author pkolmykov
 */
/**
 * No-op marker interface used for component scanning
 */
interface RepositoryPackage

interface CommentRepository : MongoRepository<Comment, String> {
    fun findAllByRecipeId(id: Int): List<Comment>
}

interface IngredientRepository : MongoRepository<Ingredient, Int> {
    fun findFirstByOrderByIdDesc(): Ingredient
}

interface RecipeRepository : MongoRepository<Recipe, Int>, RecipeRepositoryCustom {
    fun findByNameContainingIgnoreCase(namePart: String): List<Recipe>
    fun findFirstByOrderByIdDesc(): Recipe?
    @Query(value = "{ingredientsWithQuantities : {\$elemMatch : {ingredientId : ?0}}}", count = true)
    fun countByIngredientId(id: Int): Int
}

interface RecipeRepositoryCustom {
    fun findByCriteria(criteria: Criteria): List<Recipe>
    fun incrementLikes(recipeId: Int)
    fun decrementLikes(recipeId: Int)
    fun incrementViews(recipeId: Int)
}

interface RecipesStatisticsRepository : Repository<UserRecipeStats, ObjectId>

interface UserBarRepository {
    fun updateBarItem(userId: String, barItem: User.BarItem)
    fun addBarItem(userId: String, barItem: User.BarItem)
    fun removeBarItem(userId: String, ingredientId: Int)
}

interface UserRepository : CrudRepository<User, String>, UserBarRepository {
    fun findByUsername(username: String): User
    @Query("{barItems : {\$elemMatch : {ingredientId : ?0}}}")
    fun findByUserBarIngredientId(id: Int): List<User>
}
