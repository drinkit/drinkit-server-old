package guru.drinkit.service

import guru.drinkit.common.Criteria
import guru.drinkit.domain.Recipe

/**
 * @author pkolmykov
 */
interface RecipeService :CrudService<Int, Recipe>{
    fun findAll(): List<Recipe>
    fun findByCriteria(criteria: Criteria): List<Recipe>
    fun findByRecipeNameContaining(namePart: String): List<Recipe>
    fun saveMedia(recipeId: Int, image: ByteArray?, thumbnail: ByteArray?)
}


interface StatsService{
    fun addViewToRecipe(recipeId: Int, userId: String)
}
