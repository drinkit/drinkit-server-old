package guru.drinkit.springconfig;

import javax.annotation.Resource;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author pkolmykov
 */
@Configuration
@ComponentScan({
        "guru.drinkit.controller",
        "guru.drinkit.common"
})
@EnableWebMvc
@Import(RepositoryRestMvcConfiguration.class)
@EnableAspectJAutoProxy
public class WebConfig extends WebMvcConfigurerAdapter {

    public static final String REST_ENDPOINT = "/rest";
    public static final String MEDIA_ENDPOINT = "/media";
    @Resource
    Environment environment;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(MEDIA_ENDPOINT + "/**").addResourceLocations("file:///" + environment.getProperty("media.folder") + "/").setCachePeriod(60000);
    }

}
