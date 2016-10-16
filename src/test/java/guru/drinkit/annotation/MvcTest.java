package guru.drinkit.annotation;

import guru.drinkit.springconfig.AppConfig;
import guru.drinkit.springconfig.MockDBConfig;
import guru.drinkit.springconfig.SecurityConfig;
import guru.drinkit.springconfig.WebConfig;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author pkolmykov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebConfig.class, SecurityConfig.class, MockDBConfig.class, AppConfig.class})
@ActiveProfiles("test")
@WebAppConfiguration
@Retention(RetentionPolicy.RUNTIME)
public @interface MvcTest {
}
