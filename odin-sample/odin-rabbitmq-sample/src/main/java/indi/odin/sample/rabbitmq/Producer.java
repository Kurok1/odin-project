package indi.odin.sample.rabbitmq;

import indi.odin.ConnectionPool;
import indi.odin.ConnectionSource;
import indi.odin.Message;
import indi.odin.client.BasicConfiguration;
import indi.odin.client.Callback;
import indi.odin.client.Channel;
import indi.odin.client.rabbitmq.RabbitmqChannelFactory;
import indi.odin.client.rabbitmq.RabbitmqConfiguration;
import indi.odin.client.rabbitmq.RabbitmqMessageAssembler;
import indi.odin.io.Serializers;

/**
 * rabbitmq producer
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class Producer {

    private static final String connectionStr = "rabbitmq://localhost:5672/test-odin?vhost=/test-odin&username=guest&password=guest";

    public static void main(String[] args) throws Exception {
        ConnectionSource connectionSource = new ConnectionSource("test-producer", connectionStr);

        RabbitmqConfiguration configuration = (RabbitmqConfiguration) connectionSource.resolveConfiguration();
        Serializers serializers = new Serializers();
        RabbitmqMessageAssembler messageAssembler = new RabbitmqMessageAssembler(serializers);
        RabbitmqChannelFactory channelFactory = new RabbitmqChannelFactory(messageAssembler);

        Channel channel = channelFactory.create("test-producer", configuration);

        channel.sendMessage("Hello Odin", new Callback() {
            @Override
            public void onSuccess(Message message, Channel channel) {
                System.out.println("send message succeed");
            }

            @Override
            public void onFailure(Message message, Channel channel, String errorMessage) {
                System.out.printf("send message failed with error %s \n", errorMessage);
            }
        });

    }

}
