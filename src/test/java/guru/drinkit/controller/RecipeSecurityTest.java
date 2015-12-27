package guru.drinkit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.drinkit.domain.Recipe;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author pkolmykov
 */
public class RecipeSecurityTest extends MvcTestAbstract {


    @Test
    public void testGetRecipeById() throws Exception {

    }

    @Test
    public void testCreateRecipe() throws Exception {

    }

    @Test
    public void testSearchRecipes() throws Exception {

    }

    @Test
    public void testDeleteRecipe() throws Exception {

    }

    @Test
    @WithMockUser(roles = "USER")
    public void testUpdateRecipe() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1);
        mockMvc.perform(
                put("asd")
                        .content(new ObjectMapper().writeValueAsBytes(recipe))
                        .contentType(MediaType.APPLICATION_JSON)
//                .servletPath("/rest")
//                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("ADMIN_ROLE"))
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testFindRecipesByNamePart() throws Exception {

    }

    @Test
    public void testUploadMedia() throws Exception {

    }
}