package guru.drinkit.repository

import guru.drinkit.common.Criteria
import guru.drinkit.domain.Comment
import guru.drinkit.domain.Ingredient
import guru.drinkit.domain.Recipe
import guru.drinkit.domain.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.CrudRepository

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

    fun adjustLikesCount(recipeId: Int, shiftValue: Int)
    fun incrementLikes(recipeId: Int) = adjustLikesCount(recipeId, 1)
    fun decrementLikes(recipeId: Int) = adjustLikesCount(recipeId, -1)

    fun adjustViewsCount(recipeId: Int, shiftValue: Int)
    fun incrementViews(recipeId: Int) = adjustViewsCount(recipeId, 1)

}

interface UserRepositoryCustom {
    fun incrementRecipeViews(userId: String, recipeId: Int)
    fun changeRecipeLike(userId: String, recipeId: Int, liked: Boolean)
    fun updateBarItem(userId: String, barItem: User.BarItem)
    fun addBarItem(userId: String, barItem: User.BarItem)
    fun removeBarItem(userId: String, ingredientId: Int)
}

interface UserRepository : CrudRepository<User, String>, UserRepositoryCustom {
    fun findByUsername(username: String): User?
    @Query("{barItems : {\$elemMatch : {ingredientId : ?0}}}") //todo projection
    fun findByUserBarIngredientId(id: Int): List<User>
}
