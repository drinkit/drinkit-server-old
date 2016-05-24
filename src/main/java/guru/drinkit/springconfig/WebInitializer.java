package guru.drinkit.springconfig;

import javax.servlet.Filter;

import org.springframework.core.annotation.Order;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel Kolmykov
 * Date: 28.07.13
 * Time: 19:17
 */
@Order(1)
public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{AppConfig.class, MongoConfig.class, WebConfig.class, SecurityConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{WebConfig.REST_ENDPOINT + "/*"};
    }

    @Override
    protected Filter[] getServletFilters() {
        return new Filter[]{new ShallowEtagHeaderFilter()};
    }

}
