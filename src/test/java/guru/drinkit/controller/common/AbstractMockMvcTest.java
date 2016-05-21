package guru.drinkit.controller.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.Filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.drinkit.security.Role;
import guru.drinkit.springconfig.AppConfig;
import guru.drinkit.springconfig.MockDBConfig;
import guru.drinkit.springconfig.SecurityConfig;
import guru.drinkit.springconfig.WebConfig;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.MockMvcConfigurerAdapter;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.http.HttpDocumentation.httpRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author pkolmykov
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebConfig.class, SecurityConfig.class, MockDBConfig.class, AppConfig.class})
@ActiveProfiles("test")
@WebAppConfiguration
@Configurable
@DirtiesContext
public abstract class AbstractMockMvcTest {

    private static CustomWriteResolver customWriteResolver;
    @Rule
    public RestDocumentation restDocumentation = new RestDocumentation("target/generated-snippets");
    @Autowired
    protected ObjectMapper objectMapper;
    protected MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private Filter springSecurityFilterChain;

    @BeforeClass
    public static void initOnce() {
        customWriteResolver = new CustomWriteResolver();
    }

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
//                                if (mockHttpServletRequest.getServletPath().equals("/rest")) {
//                                    return mockHttpServletRequest;
//                                }
                                String requestURI = "/rest/" + mockHttpServletRequest.getRequestURI();
                                mockHttpServletRequest.setRequestURI(requestURI);
                                mockHttpServletRequest.setPathInfo(requestURI);
                                mockHttpServletRequest.setServletPath("/rest");
                                return mockHttpServletRequest;
                            }
                        };
                    }
                })
                .apply(
                        documentationConfiguration(this.restDocumentation)
                                .snippets().withDefaults(httpRequest(), httpResponse())
                                .and().writerResolver(customWriteResolver)
                )
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    protected void verifyAccess(HttpMethod httpMethod, String uri, final ResultMatcher resultMatcher, Role... allowed) throws Exception {
        verifyAccess(httpMethod, uri, null, resultMatcher, allowed);
    }

    protected void verifyAccess(HttpMethod httpMethod, String uri, Object body, final ResultMatcher resultMatcher, Role... allowed) throws Exception {
        if (allowed.length == 0) {
            allowed = Role.values();
        }
        List<Role> roles = new ArrayList<>(Arrays.asList(Role.values()));
        for (final Role role : allowed) {
            mockMvc.perform(getMockHttpServletRequestBuilder(httpMethod, uri, body)
                    .with(user("testUser").roles(role.name()))
            )
                    .andExpect(resultMatcher)
                    .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint())));
            roles.remove(role);
        }
        if (roles.size() > 0) {
            mockMvc.perform(getMockHttpServletRequestBuilder(httpMethod, uri, body)
                    .with(user("testUser").roles(CollectionUtils.collect(roles, new Transformer<Role, String>() {
                        @Override
                        public String transform(final Role role) {
                            return role.name();
                        }
                    }).toArray(new String[]{})))
            ).andExpect(status().isForbidden());
        }

    }

    private MockHttpServletRequestBuilder getMockHttpServletRequestBuilder(final HttpMethod httpMethod, final String uri, final Object body) throws JsonProcessingException {
        return request(httpMethod, uri)
                .content(objectMapper.writeValueAsBytes(body))
                .contentType(MediaType.APPLICATION_JSON);
    }

}
