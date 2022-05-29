package indi.odin.spring.autoconfigure;

import indi.odin.spring.autoconfigure.kafka.KafkaOdinAutoConfiguration;
import indi.odin.spring.autoconfigure.rabbitmq.RabbitmqOdinAutoConfiguration;
import indi.odin.spring.consumer.MessageListenerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开关
 *
 * @author <a href="mailto:hanchao@66yunlian.com">韩超</a>
 * @since 1.0.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({OdinAutoConfiguration.class, OdinListenerAutoConfiguration.class, MessageListenerRegistrar.class})
public @interface EnableOdinClient {

    String[] basePackages();

}
