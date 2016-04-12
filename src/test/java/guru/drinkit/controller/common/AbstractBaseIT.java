package guru.drinkit.controller.common;

import java.util.ArrayList;
import java.util.Arrays;

import com.mongodb.BasicDBObject;
import guru.drinkit.domain.Ingredient;
import guru.drinkit.domain.Recipe;
import guru.drinkit.domain.User;
import guru.drinkit.repository.RecipeRepository;
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

/**
 * Created with IntelliJ IDEA.
 * User: Pavel Kolmykov
 * Date: 09.11.2014
 * Time: 16:45
 */
@ContextConfiguration(classes = {AppConfig.class, MongoConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public abstract class AbstractBaseIT {
    protected Ingredient firstIngredient;
    protected Ingredient secondIngredient;
    protected User user;
    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Before
    public void initTestData() {
        cleanUp();
        firstIngredient = new Ingredient();
        firstIngredient.setDescription("firstIngredient");
        firstIngredient.setName("First");
        firstIngredient.setVol(30);
        firstIngredient = ingredientService.save(firstIngredient);

        secondIngredient = new Ingredient();
        secondIngredient.setDescription("secondIngredient");
        secondIngredient.setName("Second");
        secondIngredient.setVol(40);
        secondIngredient = ingredientService.save(secondIngredient);

        user = new User();
        user.setDisplayName("Test user");
        user.setBarItems(new ArrayList<User.BarItem>(){{
            add(new User.BarItem(firstIngredient.getId()));
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
        recipe.setOptions(new int[]{1, 2});
        recipe.setIngredientsWithQuantities(Arrays.asList(
                new Recipe.IngredientWithQuantity(firstIngredient.getId(), 50),
                new Recipe.IngredientWithQuantity(secondIngredient.getId(), 60)));
        return recipe;
    }

}
