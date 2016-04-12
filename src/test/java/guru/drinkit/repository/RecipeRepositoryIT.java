package guru.drinkit.repository;

import guru.drinkit.controller.common.AbstractBaseIT;
import guru.drinkit.domain.Recipe;
import guru.drinkit.springconfig.AppConfig;
import guru.drinkit.springconfig.MongoConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author pkolmykov
 */


@ContextConfiguration(classes = {AppConfig.class, MongoConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class RecipeRepositoryIT extends AbstractBaseIT {

    @Autowired
    RecipeRepository recipeRepository;

    @Test
    public void testUpdateRecipe() throws Exception {
        recipeRepository.update(new Recipe());

    }
}