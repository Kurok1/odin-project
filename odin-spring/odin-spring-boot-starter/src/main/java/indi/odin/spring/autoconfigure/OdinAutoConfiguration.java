package indi.odin.spring.autoconfigure;

import indi.odin.client.ChannelManager;
import indi.odin.io.Serializer;
import indi.odin.io.Serializers;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.Collection;

/**
 * 自动装配
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
@EnableConfigurationProperties(OdinProperty.class)
public class OdinAutoConfiguration {

    public static final String INITIALIZE_RABBIT_BEAN = "odinRabbitMQInitializer";
    public static final String INITIALIZE_KAFKA_BEAN = "odinKafkaInitializer";

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public Serializers serializers(ObjectProvider<Collection<Serializer<?>>> provider) {
        Collection<Serializer<?>> serializerCollection = provider.getIfAvailable();
        if (serializerCollection != null && !serializerCollection.isEmpty())
            return new Serializers(serializerCollection);

        return new Serializers();
    }

    @Primary
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    public ChannelManager channelManager() {
        return new ChannelManagerBean();
    }

}
