package guru.drinkit.repository

import guru.drinkit.AbstractBaseTest
import guru.drinkit.domain.Comment
import guru.drinkit.domain.Ingredient
import guru.drinkit.domain.Recipe
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

/**
 * @author pkolmykov
 */
class CrudRepositoriesIT : AbstractBaseTest() {

    private val INGREDIENT_ID = 123

    private val RECIPE_ID = 1

    @Autowired lateinit var commentRepository: CommentRepository
    @Autowired lateinit var recipeRepository: RecipeRepository
    @Autowired lateinit var ingredientRepository : IngredientRepository

    @Test
    fun testComment() {
        val raw = Comment(null, RECIPE_ID, Date(), Comment.Author(ObjectId().toHexString(), "4yvak"), "bla bla bla")
        val id = commentRepository.insert(raw).id
        assertThat(id).isNotNull();
        val inserted = commentRepository.findOne(id)
        assertThat(raw).isEqualTo(inserted)
        val toUpdate = inserted.copy(text = "updated text")
        commentRepository.save(toUpdate)
        assertThat(commentRepository.findOne(id)).isEqualTo(toUpdate)
        commentRepository.findAllByRecipeId(RECIPE_ID)
        commentRepository.delete(id)
        assertThat(commentRepository.findOne(id)).isNull()
    }

    @Test
    fun testRecipe() {
        val raw = Recipe(RECIPE_ID, 1, "desc", "test name", listOf(1), listOf(Recipe.IngredientWithQuantity(INGREDIENT_ID, null)), null, null, Date(), null, true, null)
        val id = recipeRepository.insert(raw).id
        assertThat(id).isNotNull();
        val inserted = recipeRepository.findOne(id)
        assertThat(raw).isEqualTo(inserted)
        val toUpdate = inserted.copy(description = "updated text")
        recipeRepository.save(toUpdate)
        assertThat(recipeRepository.findOne(id)).isEqualTo(toUpdate)

        assertThat(recipeRepository.countByIngredientId(INGREDIENT_ID)).isEqualTo(1)
        assertThat(recipeRepository.findByNameContainingIgnoreCase("test")).containsExactly(toUpdate)
        assertThat(recipeRepository.findFirstByOrderByIdDesc()).isEqualTo(toUpdate)

        recipeRepository.incrementLikes(RECIPE_ID)
        assertThat(recipeRepository.findOne(RECIPE_ID).stats?.likes).isEqualTo(1)
        recipeRepository.decrementLikes(RECIPE_ID)
        assertThat(recipeRepository.findOne(RECIPE_ID).stats?.likes).isEqualTo(0)

        recipeRepository.incrementViews(RECIPE_ID)
        assertThat(recipeRepository.findOne(RECIPE_ID).stats?.views).isEqualTo(1)

        recipeRepository.delete(id)
        assertThat(recipeRepository.findOne(id)).isNull()
    }

    @Test
    fun testIngredient() {
        val raw = Ingredient(INGREDIENT_ID, "test name", null, "desc", null, null)
        val id = ingredientRepository.insert(raw).id
        assertThat(id).isNotNull();
        val inserted = ingredientRepository.findOne(id)
        assertThat(raw).isEqualTo(inserted)
        val toUpdate = inserted.copy(description = "updated text")
        ingredientRepository.save(toUpdate)
        assertThat(ingredientRepository.findOne(id)).isEqualTo(toUpdate)

        assertThat(ingredientRepository.findFirstByOrderByIdDesc()).isEqualTo(toUpdate)

        ingredientRepository.delete(id)
        assertThat(ingredientRepository.findOne(id)).isNull()
    }


}