package indi.odin.rabbitmq.consumer;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import indi.odin.client.BasicConfiguration;
import indi.odin.consumer.AbstractListener;
import indi.odin.consumer.MessageProcessor;
import indi.odin.rabbitmq.client.RabbitmqConfiguration;
import indi.odin.rabbitmq.client.RabbitmqMessage;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * rabbitmq消费者
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.3.0
 */
public class RabbitmqListener extends AbstractListener {

    private final Channel delegateChannel;

    public RabbitmqListener(BasicConfiguration configuration) throws IOException {
        RabbitmqConfiguration rabbitmqConfiguration = (RabbitmqConfiguration) configuration;
        Connection connection = prepareConnection(rabbitmqConfiguration);
        this.delegateChannel = connection.createChannel();
    }

    protected Connection prepareConnection(RabbitmqConfiguration configuration) {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setUsername(configuration.getUsername());
            connectionFactory.setPassword(configuration.getPassword());
            connectionFactory.setVirtualHost(configuration.getVhost());

            return connectionFactory.newConnection(resolveAddressList(configuration));
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        }
    }

    protected List<Address> resolveAddressList(RabbitmqConfiguration configuration) {
        if (configuration.getServerList() == null || configuration.getServerList().isEmpty())
            throw new IllegalArgumentException("no server list");

        return configuration.getServerList().stream()
                .map(server -> new Address(server.getAddress(), server.getPort()))
                .collect(Collectors.toList());
    }

    @Override
    protected void startInternal() throws Exception {
        //监听队列
        for (String queue : registeredQueues) {
            MessageProcessor<RabbitmqMessage> messageProcessor = (MessageProcessor<RabbitmqMessage>) super.queueMessageProcessorMap.get(queue);
            this.delegateChannel.basicConsume(queue, false, buildRabbitmqConsumerAdapter(this.delegateChannel, queue, messageProcessor));
        }
    }


    private RabbitMqConsumer buildRabbitmqConsumerAdapter(Channel channel, String queueName, MessageProcessor<RabbitmqMessage> messageProcessor) {
        return new RabbitMqConsumer(queueName, channel, messageProcessor);
    }

    @Override
    protected void stopInternal() throws Exception {

    }
}
