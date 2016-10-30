package guru.drinkit;

import com.mongodb.BasicDBObject;
import guru.drinkit.domain.Ingredient;
import guru.drinkit.domain.Recipe;
import guru.drinkit.domain.User;
import guru.drinkit.repository.UserRepository;
import guru.drinkit.service.IngredientService;
import guru.drinkit.springconfig.AppConfig;
import guru.drinkit.springconfig.MongoConfig;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel Kolmykov
 * Date: 09.11.2014
 * Time: 16:45
 */
@ContextConfiguration(classes = {AppConfig.class, MongoConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public abstract class AbstractBaseTest {
    protected Ingredient firstIngredient;
    protected Ingredient secondIngredient;
    protected User user;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    private IngredientService ingredientService;
    @Autowired
    private UserRepository userRepository;

    @Before
    public void initTestData() {
        cleanUp();
        firstIngredient = new Ingredient();
        firstIngredient.setDescription("firstIngredient");
        firstIngredient.setName("First");
        firstIngredient.setVol(30);
        firstIngredient = ingredientService.insert(firstIngredient);

        secondIngredient = new Ingredient();
        secondIngredient.setDescription("secondIngredient");
        secondIngredient.setName("Second");
        secondIngredient.setVol(40);
        secondIngredient = ingredientService.insert(secondIngredient);

        user = new User();
        user.setDisplayName("Test user");
        user.setBarItems(new ArrayList<User.BarItem>(){{
            add(new User.BarItem(firstIngredient.getId(), true));
        }});
        userRepository.save(user);
    }

    private void cleanUp() {
        for (String collectionName : mongoTemplate.getCollectionNames()) {
            if (!collectionName.startsWith("system.")) {
                mongoTemplate.getCollection(collectionName).remove(new BasicDBObject());
            }
        }
    }

    protected Recipe createNewRecipeDto() {
        Recipe recipe = new Recipe();
        recipe.setCocktailTypeId(1);
        recipe.setDescription("desc");
        recipe.setName("Recipe for integration tests");
        recipe.setOptions(Arrays.asList(1, 2));
        recipe.setIngredientsWithQuantities(Arrays.asList(
            new Recipe.IngredientWithQuantity(firstIngredient.getId(), 50, null),
            new Recipe.IngredientWithQuantity(secondIngredient.getId(), 60, null)));
        return recipe;
    }

}
