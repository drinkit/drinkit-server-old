package guru.drinkit.springconfig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("guru.drinkit.service")
@PropertySource("classpath:application.properties")
public class AppConfig {
}
