package indi.odin.sample.rabbitmq;

import indi.odin.ConnectionSource;
import indi.odin.consumer.*;
import indi.odin.rabbitmq.RabbitConnectionSource;
import indi.odin.rabbitmq.client.RabbitmqConfiguration;
import indi.odin.rabbitmq.client.RabbitmqMessage;

import java.nio.charset.StandardCharsets;

/**
 * rabbitmq consumer
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class Consumer {

    private static final String connectionStr = "rabbitmq://localhost:5672/?vhost=/test-odin&username=guest&password=guest";
    private static final String bindQueue = "test-odin";

    public static void main(String[] args) throws Exception {
        final ConnectionSource<RabbitmqConfiguration> connectionSource = new RabbitConnectionSource("test-consumer", connectionStr);
        RabbitmqConfiguration configuration = (RabbitmqConfiguration) connectionSource.resolveConfiguration();

        Listener listener = ListenerFactory.createListener(connectionSource.getProduct(), configuration);

        listener.bindQueues(bindQueue);
        listener.bindMessageProcessor(bindQueue, new MessageProcessorImpl());

        ListenerExecutor.INSTANCE.addListener(listener);
    }

    public static class MessageProcessorImpl implements MessageProcessor<RabbitmqMessage> {
        private final String template = "received message from queue [%s] and deliveryTag is [%d] and message body is %s";

        @Override
        public HandleResponse onMessage(String queueName, RabbitmqMessage message) {
            String body = new String(message.getSources(), StandardCharsets.UTF_8);
            long deliveryTag = message.metaData().getDeliveryTag();
            System.out.println(String.format(template, queueName, deliveryTag, body));
            return HandleResponse.SUCCESS;
        }
    }

}
