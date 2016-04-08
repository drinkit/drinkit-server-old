package guru.drinkit.controller.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.drinkit.AbstractBaseTest;
import guru.drinkit.springconfig.WebConfig;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel Kolmykov
 * Date: 19.10.2014
 * Time: 21:53
 */
@ContextConfiguration(classes = WebConfig.class)
@WebAppConfiguration
public abstract class AbstractRestMockMvc extends AbstractBaseTest {

    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    protected final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void initMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

}
