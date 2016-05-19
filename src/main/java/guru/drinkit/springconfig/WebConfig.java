package guru.drinkit.springconfig;

import java.util.List;
import javax.annotation.Resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author pkolmykov
 */
@Configuration
@ComponentScan("guru.drinkit.controller")
@EnableWebMvc
//@EnableAspectJAutoProxy
public class WebConfig extends WebMvcConfigurerAdapter {

    public static final String REST_ENDPOINT = "/rest";
    public static final String MEDIA_ENDPOINT = "/media";
    @Resource
    Environment environment;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(MEDIA_ENDPOINT + "/**").addResourceLocations("file:///" + environment.getProperty("media.folder") + "/").setCachePeriod(7 * 24 * 60 * 60);
    }

    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder().modulesToInstall(new KotlinModule());
    }

    @Bean
    public ObjectMapper objectMapper() {
        return jackson2ObjectMapperBuilder().build();
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper());
        return converter;
    }

    /**
     * NOTE: For some reason default message converters registered before object mapper beans initialization.
     * And registered default {@link ObjectMapper}
     * To avoid this I should register it manually.
     *
     * @param converters
     */
    @Override
    public void extendMessageConverters(final List<HttpMessageConverter<?>> converters) {
        for (final HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                ((MappingJackson2HttpMessageConverter) converter).setObjectMapper(objectMapper());
            }
        }
        super.extendMessageConverters(converters);
    }
}
