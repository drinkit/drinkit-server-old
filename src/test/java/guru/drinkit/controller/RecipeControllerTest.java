package guru.drinkit.controller;

import guru.drinkit.common.Criteria;
import guru.drinkit.domain.Recipe;
import guru.drinkit.repository.RecipeRepository;
import guru.drinkit.security.Role;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static guru.drinkit.controller.RecipeController.RESOURCE_NAME;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author pkolmykov
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
public class RecipeControllerTest extends AbstractMockMvcTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Before
    public void setUp() throws Exception {
        when(recipeRepository.findOne(1)).thenReturn(new Recipe());

    }

    @Test
    public void getRecipeById() throws Exception {
        verifyAccess(GET, RESOURCE_NAME + "/1", status().isOk());
    }

    @Test
    public void createRecipe() throws Exception {
        verifyAccess(POST, RESOURCE_NAME, new Recipe(), status().isCreated(), Role.ADMIN);
    }

    @Test
    public void searchRecipes() throws Exception {
        verifyAccess(GET, RESOURCE_NAME + "?criteria=" + objectMapper.writeValueAsString(new Criteria()), status().isOk());//// TODO: 4/7/2016
    }

    @Test
    public void deleteRecipe() throws Exception {
        verifyAccess(DELETE, RESOURCE_NAME + "/1", status().isNoContent(), Role.ADMIN);
    }

    @Test
    public void updateRecipe() throws Exception {
        verifyAccess(PUT, RESOURCE_NAME + "/1", status().isNoContent(), Role.ADMIN);// TODO: 4/7/2016
    }

    @Test
    public void findRecipesByNamePart() throws Exception {
        verifyAccess(GET, RESOURCE_NAME + "/1", status().isOk());
    }

    @Test
    public void uploadMedia() throws Exception {
        verifyAccess(PUT, RESOURCE_NAME + "/1", status().isNoContent(), Role.ADMIN);/// TODO: 4/7/2016
    }

}