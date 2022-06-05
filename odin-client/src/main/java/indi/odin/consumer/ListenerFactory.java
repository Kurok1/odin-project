package indi.odin.consumer;

import indi.odin.SupportProduct;
import indi.odin.client.BasicConfiguration;
import indi.odin.client.rabbitmq.RabbitmqConfiguration;
import indi.odin.consumer.rabbitmq.RabbitmqListener;

/**
 * 监听这创建工厂
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class ListenerFactory {


    public static Listener createListener(SupportProduct supportProduct, BasicConfiguration configuration) throws Exception {
        switch (supportProduct) {
            case RABBITMQ: return new RabbitmqListener((RabbitmqConfiguration) configuration);
            case KAFKA:
            case ROCKETMQ:
            default: throw new UnsupportedOperationException();
        }
    }

}
