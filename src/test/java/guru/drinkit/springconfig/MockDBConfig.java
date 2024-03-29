package guru.drinkit.springconfig;

import static guru.drinkit.springconfig.mock.MockitoBeanDefinitionRegistryHelper.registerMock;

import guru.drinkit.repository.RepositoryPackage;
import guru.drinkit.springconfig.mock.RepositoryComponentProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.Collections;
import java.util.Set;

/**
 * @author pkolmykov
 */
@Configuration
@ComponentScan("guru.drinkit.repository")
public class MockDBConfig {

    @Bean
    public static BeanDefinitionRegistryPostProcessor beanDefinitionRegistryPostProcessor() {
        return new BeanDefinitionRegistryPostProcessor() {
            @Override
            public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {
            }

            @Override
            public void postProcessBeanDefinitionRegistry(final BeanDefinitionRegistry registry) throws BeansException {
                registerRepositories(registry);
                registerMock(MongoOperations.class, registry);
            }

        };
    }

    private static void registerRepositories(final BeanDefinitionRegistry registry) {
        final Class<RepositoryPackage> repositoryPackageClass = RepositoryPackage.class;
        final RepositoryComponentProvider provider = new RepositoryComponentProvider(Collections.<TypeFilter>emptyList());
        final Set<BeanDefinition> candidateComponents = provider.findCandidateComponents(repositoryPackageClass.getPackage().getName());
        for (BeanDefinition candidateComponent : candidateComponents) {
            registerMock(candidateComponent, registry);
        }
    }

}