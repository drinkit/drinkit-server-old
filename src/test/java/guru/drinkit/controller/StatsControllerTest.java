package guru.drinkit.controller;

import guru.drinkit.common.DrinkitUtils;
import guru.drinkit.controller.common.AbstractMockMvcTest;
import guru.drinkit.repository.RecipeRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author pkolmykov
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
public class StatsControllerTest extends AbstractMockMvcTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    public void incrementViewsCount() throws Exception {
        mockMvc.perform(patch("/users/me/stats/1")
        ).andExpect(status().isNoContent());
        assertThat(DrinkitUtils.getUserName()).isEqualTo("ANONYMOUS");
        verify(recipeRepository).incrementViews(1);
    }

}