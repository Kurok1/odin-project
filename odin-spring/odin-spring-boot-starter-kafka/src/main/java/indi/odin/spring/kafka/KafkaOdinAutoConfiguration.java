package indi.odin.spring.kafka;

import indi.odin.kafka.client.KafkaChannelFactory;
import indi.odin.kafka.client.KafkaMessageAssembler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

import static indi.odin.spring.autoconfigure.OdinAutoConfiguration.INITIALIZE_KAFKA_BEAN;

/**
 * Kafka自动装配
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class KafkaOdinAutoConfiguration implements BeanDefinitionRegistryPostProcessor {

    private final static Logger logger = LoggerFactory.getLogger(KafkaOdinAutoConfiguration.class);

    @ConditionalOnClass(name = "indi.odin.kafka.client.KafkaChannelFactory")
    @Bean(name = INITIALIZE_KAFKA_BEAN)
    public Object odinKafkaInitializer(KafkaConnectionRegister connectionRegister, KafkaChannelFactory kafkaChannelFactory) {
        return new Object();
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        logger.info("register KafkaMessageAssembler");
        registerKafkaMessageAssembler(registry);
        logger.info("register KafkaChannelFactory");
        registerKafkaChannelFactory(registry);
    }

    private void registerKafkaChannelFactory(BeanDefinitionRegistry registry) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(KafkaChannelFactoryBean.class);
        beanDefinition.setTargetType(KafkaChannelFactory.class);
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        registry.registerBeanDefinition("kafkaChannelFactory", beanDefinition);
    }

    private void registerKafkaMessageAssembler(BeanDefinitionRegistry registry) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setTargetType(KafkaMessageAssembler.class);
        beanDefinition.setBeanClass(KafkaMessageAssembler.class);
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        registry.registerBeanDefinition("kafkaMessageAssembler", beanDefinition);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
