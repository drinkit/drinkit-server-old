package guru.drinkit.controller;

import javax.servlet.Filter;

import guru.drinkit.springconfig.AppConfig;
import guru.drinkit.springconfig.MockDBConfig;
import guru.drinkit.springconfig.SecurityConfig;
import guru.drinkit.springconfig.WebConfig;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
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

/**
 * @author pkolmykov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebConfig.class, SecurityConfig.class, MockDBConfig.class, AppConfig.class, SecurityConfig.class})
@WebAppConfiguration
public class MvcTestAbstract {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;


    protected MockMvc mockMvc;

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
}
