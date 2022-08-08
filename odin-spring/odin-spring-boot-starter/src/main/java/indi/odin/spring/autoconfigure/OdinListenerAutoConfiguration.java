package indi.odin.spring.autoconfigure;

import indi.odin.ConnectionPool;
import indi.odin.consumer.Listener;
import indi.odin.consumer.ListenerExecutor;
import indi.odin.spring.consumer.MessageListenerPostProcessor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 监听者自动装配
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
@Configuration
public class OdinListenerAutoConfiguration {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 10)
    public ListenerExecutor listenerExecutor(ObjectProvider<Listener> listeners) {
        ListenerExecutor executor = ListenerExecutor.INSTANCE;
        listeners.stream().forEach(executor::addListener);
        return executor;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 10)
    public MessageListenerPostProcessor messageListenerPostProcessor() {
        ConnectionPool connectionPool = ConnectionPool.INSTANCE;
        return new MessageListenerPostProcessor(connectionPool);
    }

}
