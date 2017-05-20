package guru.drinkit

import com.mongodb.BasicDBObject
import guru.drinkit.domain.Ingredient
import guru.drinkit.domain.Recipe
import guru.drinkit.domain.User
import guru.drinkit.repository.UserRepository
import guru.drinkit.service.IngredientService
import guru.drinkit.springconfig.AppConfig
import guru.drinkit.springconfig.LuceneConfig
import guru.drinkit.springconfig.MongoConfig
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import java.util.*

/**
 * Created with IntelliJ IDEA.
 * User: Pavel Kolmykov
 * Date: 09.11.2014
 * Time: 16:45
 */
@ContextConfiguration(classes = arrayOf(AppConfig::class, MongoConfig::class, LuceneConfig::class))
@RunWith(SpringJUnit4ClassRunner::class)
@ActiveProfiles("test")
abstract class AbstractBaseTest {
    protected lateinit var firstIngredient: Ingredient
    protected lateinit var secondIngredient: Ingredient
    protected lateinit var user: User
    @Autowired
    internal var mongoTemplate: MongoTemplate? = null
    @Autowired
    private val ingredientService: IngredientService? = null
    @Autowired
    private val userRepository: UserRepository? = null

    @Before
    fun initTestData() {
        cleanUp()
        firstIngredient = Ingredient()
        firstIngredient.description = "firstIngredient"
        firstIngredient.name = "First"
        firstIngredient.vol = 30
        firstIngredient = ingredientService!!.insert(firstIngredient)

        secondIngredient = Ingredient()
        secondIngredient.description = "secondIngredient"
        secondIngredient.name = "Second"
        secondIngredient.vol = 40
        secondIngredient = ingredientService.insert(secondIngredient)

        user = User()
        user.displayName = "Test user"
        user.barItems = object : ArrayList<User.BarItem>() {
            init {
                add(User.BarItem(firstIngredient.id!!, true))
            }
        }
        userRepository!!.save(user)
    }

    private fun cleanUp() {
        for (collectionName in mongoTemplate!!.collectionNames) {
            if (!collectionName.startsWith("system.")) {
                mongoTemplate!!.getCollection(collectionName).remove(BasicDBObject())
            }
        }
    }

    protected fun createNewRecipeDto(): Recipe {
        val recipe = Recipe(
                cocktailTypeId = 1,
                description = "desc",
                name = "Recipe for integration tests",
                options = Arrays.asList(1, 2),
                ingredientsWithQuantities = Arrays.asList(
                        Recipe.IngredientWithQuantity(firstIngredient.id!!, 50, null),
                        Recipe.IngredientWithQuantity(secondIngredient.id!!, 60, null)))
        return recipe
    }

}
