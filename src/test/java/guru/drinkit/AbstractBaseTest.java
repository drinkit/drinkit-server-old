package guru.drinkit;

import guru.drinkit.domain.Ingredient;
import guru.drinkit.domain.Recipe;
import guru.drinkit.service.IngredientService;
import guru.drinkit.springconfig.AppConfig;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel Kolmykov
 * Date: 09.11.2014
 * Time: 16:45
 */
@ContextConfiguration(classes = {AppConfig.class})
@Transactional()
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public abstract class AbstractBaseTest {
    protected Ingredient firstIngredient;
    protected Ingredient secondIngredient;
    @Autowired
    private IngredientService ingredientService;

    @Autowired
    MongoTemplate mongoTemplate;

    @Before
    public void initTestData() {
        mongoTemplate.getDb().dropDatabase();
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
    }

    protected Recipe createNewRecipeDto() {
        Recipe recipe = new Recipe();
        recipe.setCocktailTypeId(1);
        recipe.setDescription("desc");
        recipe.setName("Recipe for integration tests");
        recipe.setOptions(new int[]{1, 2});
        recipe.setCocktailIngredients(new Integer[][]{{firstIngredient.getId(), 50}, {secondIngredient.getId(), 60}});
        return recipe;
    }

}
