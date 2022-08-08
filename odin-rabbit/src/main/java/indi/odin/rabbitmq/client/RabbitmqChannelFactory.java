package indi.odin.rabbitmq.client;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import indi.odin.client.ChannelFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * rabbitmq管道工厂
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.3.0
 */
public class RabbitmqChannelFactory implements ChannelFactory<RabbitmqChannel, RabbitmqConfiguration> {

    private final RabbitmqMessageAssembler assembler;

    public RabbitmqChannelFactory(RabbitmqMessageAssembler assembler) {
        this.assembler = assembler;
    }

    @Override
    public RabbitmqChannel create(String name, RabbitmqConfiguration configuration) throws IOException {
        Connection connection = prepareConnection(configuration);

        Channel channel = connection.createChannel();

        return new RabbitmqChannel(name, configuration.getQueueName(), channel, assembler);
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
}
