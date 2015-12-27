package guru.drinkit.controller;

import guru.drinkit.domain.Ingredient;
import guru.drinkit.repository.IngredientRepository;
import guru.drinkit.springconfig.MockDBConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by pkolmykov on 22.11.2015.
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MockDBConfig.class})
public class RepositoriesTest {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private MongoOperations mongoOperations;

    @Test
    public void testName() throws Exception {
        assertThat(ingredientRepository.findFirstByOrderByIdDesc()).isNull();
        when(ingredientRepository.findFirstByOrderByIdDesc()).thenReturn(new Ingredient());
        assertThat(ingredientRepository.findFirstByOrderByIdDesc()).isNotNull();
    }

    @Test
    public void testName2() throws Exception {
        assertThat(ingredientRepository.findFirstByOrderByIdDesc()).isNull();
        when(ingredientRepository.findFirstByOrderByIdDesc()).thenReturn(new Ingredient());
        assertThat(ingredientRepository.findFirstByOrderByIdDesc()).isNotNull();
    }
}
