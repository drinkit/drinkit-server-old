package guru.drinkit.repository

import guru.drinkit.AbstractBaseTest
import guru.drinkit.domain.Comment
import guru.drinkit.domain.Ingredient
import guru.drinkit.domain.Recipe
import guru.drinkit.domain.User
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

/**
 * @author pkolmykov
 */

private val INGREDIENT_ID = 123

private val RECIPE_ID = 1

class CommentRepositoryCrudIT : AbstractBaseTest(){

    @Autowired lateinit var commentRepository: CommentRepository

    @Test
    fun testComment() {
        val raw = Comment(null, RECIPE_ID, Date(), Comment.Author(ObjectId().toHexString(), "4yvak"), "bla bla bla")
        val id = commentRepository.insert(raw).id
        assertThat(id).isNotNull()
        val inserted = commentRepository.findOne(id)
        assertThat(raw).isEqualTo(inserted)
        val toUpdate = inserted.copy(text = "updated text")
        commentRepository.save(toUpdate)
        assertThat(commentRepository.findOne(id)).isEqualTo(toUpdate)
        commentRepository.findAllByRecipeId(RECIPE_ID)
        commentRepository.delete(id)
        assertThat(commentRepository.findOne(id)).isNull()
    }
}

class RecipeRepositoryCrudIT : AbstractBaseTest(){

    @Autowired lateinit var recipeRepository: RecipeRepository

    @Test
    fun testRecipe() {
        val raw = Recipe(RECIPE_ID, 1, "desc", "test name", "test original name", listOf(1), listOf(Recipe.IngredientWithQuantity(INGREDIENT_ID, null, null)), null, null, Date(), null, true, null)
        val id = recipeRepository.insert(raw).id
        assertThat(id).isNotNull()
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
}


class UserRepositoryCrudIT : AbstractBaseTest() {

    @Autowired lateinit var userRepository: UserRepository

    @Test
    fun testUser() {
        val username = "name"
        val raw = User(username, "pass", "disp", User.AuthorityRole.USER, emptyList(), emptyMap())
        userRepository.save(raw)
        val inserted = userRepository.findOne(username)
        assertThat(raw).isEqualTo(inserted)
        val toUpdate = inserted.copy(displayName = "updated text")
        userRepository.save(toUpdate)
        assertThat(userRepository.findOne(username)).isEqualTo(toUpdate)

        assertThat(userRepository.findByUsername(username)).isEqualTo(toUpdate)

        userRepository.incrementRecipeViews(username, 2)
        assertThat(userRepository.findOne(username).recipeStatsMap[2]?.views).isEqualTo(1)
        assertThat(userRepository.changeRecipeLike(username, 2, true))
        assertThat(userRepository.findOne(username).recipeStatsMap[2]?.liked).isTrue()
        val barItem = User.BarItem(2, true)
        userRepository.addBarItem(username, barItem)
        assertThat(userRepository.findByUserBarIngredientId(2)).isNotEmpty()
        assertThat(userRepository.findOne(username).barItems).containsExactly(barItem)
        assertThat(userRepository.updateBarItem(username, barItem.copy(active = false)))
        assertThat(userRepository.findOne(username).barItems[0].active).isFalse()
        userRepository.removeBarItem(username, 2)
        assertThat(userRepository.findOne(username).barItems).isEmpty()

        userRepository.delete(username)
        assertThat(userRepository.findOne(username)).isNull()
    }
}

class IngredientRepositoryCrudIT : AbstractBaseTest() {

    @Autowired lateinit var ingredientRepository: IngredientRepository

    @Test
    fun testIngredient() {
        val raw = Ingredient(INGREDIENT_ID, "test name", null, "desc", null, null)
        val id = ingredientRepository.insert(raw).id
        assertThat(id).isNotNull()
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