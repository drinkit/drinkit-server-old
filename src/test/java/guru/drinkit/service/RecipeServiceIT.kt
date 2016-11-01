package guru.drinkit.service

import guru.drinkit.AbstractBaseTest
import guru.drinkit.domain.Recipe
import guru.drinkit.repository.RecipeRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author pkolmykov
 */
class RecipeServiceIT : AbstractBaseTest() {

    @Autowired lateinit var recipeService: RecipeService
    @Autowired lateinit var recipeRepository: RecipeRepository

    @Test
    fun recipePersistenceTest() {
        val id = recipeService.insert(Recipe(null, 1, "desc", "name", listOf(1),
                listOf(Recipe.IngredientWithQuantity(1, 10, null)),
                null, null, null, null, true, null)).id!!
        val recipe = recipeService.find(id)!!
        assertThat(recipe.stats?.views).isEqualTo(0)
        assertThat(recipe.stats?.likes).isEqualTo(0)
        recipeRepository.incrementLikes(1)
        assertThat(recipeService.find(id)?.stats?.likes).isEqualTo(1)
        recipeService.update(id, recipe.copy(stats = null))
        assertThat(recipeService.find(id)?.stats?.likes).isEqualTo(1)
    }

}

