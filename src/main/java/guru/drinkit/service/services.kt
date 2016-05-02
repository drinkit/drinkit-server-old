package guru.drinkit.service

import guru.drinkit.common.Criteria
import guru.drinkit.domain.Comment
import guru.drinkit.domain.Entity
import guru.drinkit.domain.Ingredient
import guru.drinkit.domain.Recipe
import guru.drinkit.exception.RecordNotFoundException
import org.springframework.security.access.prepost.PreAuthorize

/**
 * @author pkolmykov
 */
interface RecipeService : CrudService<Int, Recipe> {
    fun findByCriteria(criteria: Criteria): List<Recipe>
    fun findByRecipeNameContaining(namePart: String): List<Recipe>
    fun saveMedia(recipeId: Int, image: ByteArray, thumbnail: ByteArray)
}

interface IngredientService : CrudService<Int, Ingredient>

interface StatsService {
    fun addViewToRecipe(recipeId: Int, userId: String)
}

interface CommentService : CrudService<String, Comment> {
    fun findAllByRecipeId(recipeId: Int): List<Comment>
}

interface FileStoreService {
    fun save(recipeId: Int, image: ByteArray, mediaType: String): String
    fun getUrl(fileName: String): String
}

interface CrudService<ID, T : Entity<ID>> {

    fun findAll(): List<T>
    fun find(id: ID): T?

    /**
     * @throws RecordNotFoundException if recipe not exist
     */
    fun findSafe(id: ID): T = find(id) ?: throw RecordNotFoundException("")

    fun insert(entity: T): T = if (entity.id == null) save(entity) else throw IllegalArgumentException("")

    fun update(id: Any, entity: T): T = if (id == entity.id) save(entity) else throw IllegalArgumentException()

    @PreAuthorize("isAuthenticated()")
    fun save(entity: T): T

    /**
     * @throws RecordNotFoundException if recipe not exist
     */
    fun delete(id: ID) = delete(findSafe(id))

    fun delete(entity: T)

}
