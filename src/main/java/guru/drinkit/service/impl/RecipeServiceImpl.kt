package guru.drinkit.service.impl

import guru.drinkit.common.Criteria
import guru.drinkit.common.DrinkitUtils
import guru.drinkit.common.RecipeComparatorByCriteria
import guru.drinkit.domain.Recipe
import guru.drinkit.repository.RecipeRepository
import guru.drinkit.service.FileStoreService
import guru.drinkit.service.RecipeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.Assert
import java.util.*
import javax.annotation.Resource


@Service
@Transactional
class RecipeServiceImpl @Autowired constructor(
        @Resource val fileStoreService: FileStoreService,
        @Resource val recipeRepository: RecipeRepository
) : RecipeService {

    override fun insert(recipe: Recipe): Recipe {
        Assert.isNull(recipe.id)
        val lastRecipeId = recipeRepository.findFirstByOrderByIdDesc()?.id ?: 0
        recipe.id = lastRecipeId + 1;
        recipe.addedBy = DrinkitUtils.getUserName()
        recipe.createdDate = Date()
        recipe.stats = Recipe.Stats(0, 0)
        return recipeRepository.save(recipe)
    }

    override fun update(recipe: Recipe): Recipe {
        Assert.notNull(recipe.id)
        val original = recipeRepository.findOne(recipe.id)!!
        recipeRepository.save(recipe.copy(
                stats = original.stats,
                createdDate = original.createdDate,
                addedBy = original.addedBy))
        return recipeRepository.findOne(recipe.id)
    }

    override fun delete(id: Int) = recipeRepository.delete(id)

    @Transactional(readOnly = true)
    override fun findAll() = recipeRepository.findAll()

    @Transactional(readOnly = true)
    override fun findByCriteria(criteria: Criteria): List<Recipe> {
        val recipes = recipeRepository.findByCriteria(criteria)
        Collections.sort(recipes, RecipeComparatorByCriteria(criteria))
        return recipes
    }

    @Transactional(readOnly = true)
    override fun findById(id: Int) = recipeRepository.findOne(id)

    override fun findByRecipeNameContaining(namePart: String) =
            recipeRepository.findByNameContainingIgnoreCase(namePart)

    @Transactional
    override fun saveMedia(recipeId: Int, image: ByteArray?, thumbnail: ByteArray?) {
        val recipe = recipeRepository.findOne(recipeId)
        recipe.imageUrl = fileStoreService.getUrl(fileStoreService.save(recipeId, image, "image"))
        recipe.thumbnailUrl = fileStoreService.getUrl(fileStoreService.save(recipeId, thumbnail, "thumbnail"))
        recipeRepository.save(recipe)
    }
}
