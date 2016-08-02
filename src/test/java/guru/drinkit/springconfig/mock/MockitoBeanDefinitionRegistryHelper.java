package guru.drinkit.springconfig.mock;

import org.mockito.Mockito;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

/**
 * Created by Naiv on 22.11.2015.
 */
public class MockitoBeanDefinitionRegistryHelper {

    public static final String MOCK_IDENTIFIER = "mock";

    public static void registerMock(BeanDefinition beanClass, BeanDefinitionRegistry registry) {
        try {
            registerMock(Class.forName(beanClass.getBeanClassName()), registry);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void registerMock(Class beanClass, BeanDefinitionRegistry registry) {
        GenericBeanDefinition mockBeanDefinition = getMockBeanDefinition(beanClass);
        registry.registerBeanDefinition(
                beanClass.getName() + BeanDefinitionReaderUtils.GENERATED_BEAN_NAME_SEPARATOR + MOCK_IDENTIFIER,
                mockBeanDefinition);
    }

    private static GenericBeanDefinition getMockBeanDefinition(Class<?> beanClass) {
        final GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(Mockito.class);
        final ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
        constructorArgumentValues.addGenericArgumentValue(beanClass);
        beanDefinition.setFactoryMethodName("mock");
        beanDefinition.setConstructorArgumentValues(constructorArgumentValues);
        beanDefinition.setScope(SCOPE_SINGLETON);
        return beanDefinition;
    }
}
