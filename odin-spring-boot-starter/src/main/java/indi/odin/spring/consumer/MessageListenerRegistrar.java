package indi.odin.spring.consumer;

import indi.odin.spring.autoconfigure.EnableOdinClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * {@link MessageListener} registrar
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class MessageListenerRegistrar implements ImportBeanDefinitionRegistrar,
        ResourceLoaderAware, EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(MessageListenerRegistrar.class);

    private ResourceLoader resourceLoader;
    private Environment environment;

    private TypeFilter typeFilter = new AnnotationTypeFilter(MessageListener.class);

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        Map<String, Object> packageSets = importingClassMetadata.getAnnotationAttributes(EnableOdinClient.class.getName());
        if (CollectionUtils.isEmpty(packageSets)) {
            logger.info("not found message listeners packages, did you forget?");
            return;
        }

        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry, false, environment, resourceLoader);
        scanner.addIncludeFilter(this.typeFilter);


        String[] basePackageNames = (String[]) packageSets.get("basePackages");

        scanner.scan(basePackageNames);
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
