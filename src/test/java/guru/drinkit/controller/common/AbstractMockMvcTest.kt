package guru.drinkit.controller.common

import com.fasterxml.jackson.databind.ObjectMapper
import guru.drinkit.security.Role
import guru.drinkit.springconfig.*
import org.apache.commons.collections4.CollectionUtils
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.restdocs.JUnitRestDocumentation
import org.springframework.restdocs.http.HttpDocumentation.httpRequest
import org.springframework.restdocs.http.HttpDocumentation.httpResponse
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.RequestPostProcessor
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.MockMvcConfigurerAdapter
import org.springframework.web.context.WebApplicationContext
import java.util.*
import javax.servlet.Filter


/**
 * @author pkolmykov
 */
@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(classes = arrayOf(WebConfig::class, SecurityConfig::class, MockDBConfig::class, AppConfig::class, LuceneConfig::class))
@ActiveProfiles("test")
@WebAppConfiguration
abstract class AbstractMockMvcTest {

    companion object {
        private val customWriteResolver = CustomWriteResolver()
        const val USER_NAME = "testUser"
    }

    @get:Rule
    var restDocumentation = JUnitRestDocumentation("target/generated-snippets")
    @Autowired
    lateinit protected var objectMapper: ObjectMapper
    lateinit protected var mockMvc: MockMvc
    @Autowired
    private val context: WebApplicationContext? = null
    @Autowired
    private val springSecurityFilterChain: Filter? = null


    @Before
    fun setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).addFilter<DefaultMockMvcBuilder>(springSecurityFilterChain).apply<DefaultMockMvcBuilder>(object : MockMvcConfigurerAdapter() {
            override fun beforeMockMvcCreated(configurableMockMvcBuilder: ConfigurableMockMvcBuilder<*>?, webApplicationContext: WebApplicationContext?): RequestPostProcessor {
                return RequestPostProcessor { mockHttpServletRequest ->
                    //                                if (mockHttpServletRequest.getServletPath().equals("/rest")) {
                    //                                    return mockHttpServletRequest;
                    //                                }
                    val requestURI = "/rest/" + mockHttpServletRequest.requestURI
                    mockHttpServletRequest.requestURI = requestURI
                    mockHttpServletRequest.pathInfo = requestURI
                    mockHttpServletRequest.servletPath = "/rest"
                    mockHttpServletRequest
                }
            }
        }).apply<DefaultMockMvcBuilder>(
                documentationConfiguration(this.restDocumentation).snippets().withDefaults(httpRequest(), httpResponse()).and().writerResolver(customWriteResolver)).apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity()).build()
    }


    fun verifyAccess(mockHttpServletRequestBuilder: () -> MockHttpServletRequestBuilder, resultMatcher: ResultMatcher, vararg allowed: Role = Role.values()) {
        var documented = false

        val roles = ArrayList(Arrays.asList(*Role.values()))

        for (role in allowed) {
            val actions = mockMvc.perform(mockHttpServletRequestBuilder.invoke().with(user(USER_NAME).roles(role.name))).andExpect(resultMatcher)
            if (!documented) {

                actions
                        .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint(), removeHeaders())
                        ))
                documented = true
            }

            roles.remove(role)
        }
        if (roles.size > 0) {
            mockMvc.perform(
                    mockHttpServletRequestBuilder.invoke()
                            .with(user("testUser").roles(*CollectionUtils.collect(roles) { role -> role.name }.toTypedArray())

                            )).andExpect(status().isForbidden)
        }

    }

    fun <T> anyObject(): T {
        Mockito.anyObject<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T

}
