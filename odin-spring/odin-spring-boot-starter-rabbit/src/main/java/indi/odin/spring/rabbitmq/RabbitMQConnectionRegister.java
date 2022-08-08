package indi.odin.spring.rabbitmq;

import indi.odin.SupportProduct;
import indi.odin.rabbitmq.RabbitConnectionSource;
import indi.odin.rabbitmq.client.RabbitmqConfiguration;
import indi.odin.spring.autoconfigure.ConnectionRegister;

import java.net.URL;

/**
 * rabbit connection register
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.3.0
 */
public class RabbitMQConnectionRegister extends ConnectionRegister<RabbitmqConfiguration, RabbitConnectionSource> {

    private final String RABBIT_PROTOCOL = SupportProduct.RABBITMQ.getProtocol();

    @Override
    protected String getSupportProductProtocol() {
        return RABBIT_PROTOCOL;
    }

    @Override
    protected RabbitConnectionSource toConnectionSource(String name, URL url) {
        return new RabbitConnectionSource(name, url);
    }
}
