package guru.drinkit.controller;

import javax.servlet.Filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.drinkit.domain.Recipe;
import guru.drinkit.springconfig.AppConfig;
import guru.drinkit.springconfig.SecurityConfig;
import guru.drinkit.springconfig.WebConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.MockMvcConfigurerAdapter;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author pkolmykov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebConfig.class, SecurityConfig.class, AppConfig.class})
@WebAppConfiguration
public class RecipeControllerSecurityTest {


    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;


    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilter(springSecurityFilterChain)
                .apply(new MockMvcConfigurerAdapter() {
                    @Override
                    public RequestPostProcessor beforeMockMvcCreated(final ConfigurableMockMvcBuilder<?> configurableMockMvcBuilder, final WebApplicationContext webApplicationContext) {
                        return new RequestPostProcessor() {
                            @Override
                            public MockHttpServletRequest postProcessRequest(final MockHttpServletRequest mockHttpServletRequest) {
                                mockHttpServletRequest.setRequestURI("/rest/" + RecipeController.RESOURCE_NAME + "/" + 1);
                                mockHttpServletRequest.setPathInfo("/rest/" + RecipeController.RESOURCE_NAME + "/" + 1);
                                mockHttpServletRequest.setServletPath("/rest");
                                return mockHttpServletRequest;
                            }
                        };
                    }
                })
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

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