package indi.odin.spring.autoconfigure.rabbitmq;

import indi.odin.client.rabbitmq.RabbitmqChannelFactory;
import indi.odin.client.rabbitmq.RabbitmqMessageAssembler;
import indi.odin.spring.autoconfigure.OdinProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * rabbitmq相关自动装配
 *
 * @author <a href="mailto:hanchao@66yunlian.com">韩超</a>
 * @since 1.0.0
 */
public class RabbitmqOdinAutoConfiguration implements BeanDefinitionRegistryPostProcessor {

    private final static Logger logger = LoggerFactory.getLogger(RabbitmqOdinAutoConfiguration.class);

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        //注册
        logger.info("register RabbitmqMessageAssembler");
        registerRabbitmqMessageAssembler(registry);
        logger.info("register RabbitmqChannelFactory");
        registerRabbitmqChannelFactory(registry);
    }

    private void registerRabbitmqMessageAssembler(BeanDefinitionRegistry registry) {
        RootBeanDefinition assembler = new RootBeanDefinition();
        assembler.setTargetType(RabbitmqMessageAssembler.class);
        assembler.setBeanClass(RabbitmqMessageAssembler.class);
        assembler.setScope(BeanDefinition.SCOPE_SINGLETON);
        registry.registerBeanDefinition("rabbitmqMessageAssembler", assembler);
    }

    private void registerRabbitmqChannelFactory(BeanDefinitionRegistry registry) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(RabbitmqChannelFactoryBean.class);
        beanDefinition.setTargetType(RabbitmqChannelFactory.class);
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        registry.registerBeanDefinition("rabbitmqChannelFactory", beanDefinition);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
