package guru.drinkit.service.impl

import guru.drinkit.domain.Recipe
import guru.drinkit.lucene.SearchService
import guru.drinkit.repository.RecipeRepository
import guru.drinkit.service.*
import org.apache.commons.collections4.CollectionUtils.collect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import java.util.*


@Service
open class RecipeServiceImpl
@Autowired constructor(
        private val fileStoreService: FileStoreService,
        private val recipeRepository: RecipeRepository,
        private val searchService: SearchService
) : RecipeService {

    override fun insert(entity: Recipe): Recipe {
        Assert.isNull(entity.id, "Id should be empty for insert")
        val lastRecipeId = recipeRepository.findFirstByOrderByIdDesc()?.id ?: 0
        entity.id = lastRecipeId + 1
        entity.addedBy = getUsername()
        entity.createdDate = Date()
        entity.stats = Recipe.Stats(0, 0)
        val savedRecipe = recipeRepository.save(entity)
        searchService.indexRecipe(savedRecipe)
        return savedRecipe
    }

    override fun save(entity: Recipe): Recipe {
        Assert.notNull(entity.id, "Id should be provided for update")
        val original = find(entity.id!!)
        recipeRepository.save(entity.copy(
                stats = original.stats,
                createdDate = original.createdDate,
                addedBy = original.addedBy))
        return recipeRepository.findOne(entity.id)
    }

    override fun delete(entity: Recipe) {
        recipeRepository.delete(entity)
    }

    override fun findAll(): List<Recipe> = recipeRepository.findAll()

    override fun findByCriteria(criteria: Criteria): List<Recipe> {
        val recipes = recipeRepository.findByCriteria(criteria)
        Collections.sort(recipes, RecipeComparatorByCriteria(criteria))
        return recipes
    }

    class RecipeComparatorByCriteria(val criteria: Criteria) : Comparator<Recipe> {

        var likesFactor: Int = 20

        override fun compare(recipe1: Recipe, recipe2: Recipe): Int {
            var result = getNotMatchesIngredientsCount(recipe1) - getNotMatchesIngredientsCount(recipe2)
            if (result == 0) {
                val weight1 = (recipe1.stats?.views ?: 0) + ((recipe1.stats?.likes ?: 0) * likesFactor)
                val weight2 = (recipe2.stats?.views ?: 0) + ((recipe2.stats?.likes ?: 0) * likesFactor)
                result = weight2 - weight1
            }
            return result
        }

        private fun getNotMatchesIngredientsCount(recipe: Recipe): Int {
            val ingredientsIdsFromRecipe = collect<Recipe.IngredientWithQuantity, Int>(recipe.ingredientsWithQuantities,
                    { it -> it.ingredientId })
            ingredientsIdsFromRecipe.removeAll(criteria.ingredients)
            return ingredientsIdsFromRecipe.size
        }
    }

    override fun find(id: Int) = recipeRepository.findOne(id) ?: throw RecordNotFoundException("Recipe not found")

    override fun findByRecipeNameContaining(namePart: String): List<Recipe> {
        val recipeIds = searchService.findRecipes(namePart)
        if (recipeIds.isEmpty()) {
            return emptyList()
        } else {
            val recipes = recipeRepository.findByIdIn(recipeIds).associateBy { it.id }
            return recipeIds.mapNotNull { recipes[it] }
        }
    }

    override fun saveMedia(recipeId: Int, image: ByteArray, thumbnail: ByteArray) {
        val recipe = find(recipeId)
        recipe.imageUrl = fileStoreService.getUrl(fileStoreService.save(recipeId, image, "image"))
        recipe.thumbnailUrl = fileStoreService.getUrl(fileStoreService.save(recipeId, thumbnail, "thumbnail"))
        recipeRepository.save(recipe)
    }
}
