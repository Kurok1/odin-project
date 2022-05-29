package indi.odin;

import indi.odin.client.BasicConfiguration;
import indi.odin.client.kafka.KafkaConfiguration;
import indi.odin.client.rabbitmq.RabbitmqConfiguration;
import indi.odin.exception.NoSupportedMQException;
import org.apache.kafka.clients.producer.Partitioner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * 连接源
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class ConnectionSource {

    private final String name;
    private final URL url;

    private final Map<String, String> params = new HashMap<>();
    private String host;
    private int port;
    private SupportProduct product;
    private String path;

    public ConnectionSource(String name, String url) throws MalformedURLException {
        this(name, new URL(null, url, new OdinURLStreamHandler()));
    }

    public ConnectionSource(String name, URL url) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(url);
        this.name = name;
        this.url = url;
        readUrl();
    }

    private void readUrl() {
        UrlReader reader = new UrlReader(this.url);

        this.host = reader.getHost();
        this.port = reader.getPort();
        this.path = reader.getPath();
        this.product = SupportProduct.read(reader.getProtocol());

        Collection<String> keys = reader.getKeys();

        for (String key : keys)
            this.params.put(key, reader.getValue(key));
    }

    public BasicConfiguration.ServerConfiguration getServer() {
        BasicConfiguration.ServerConfiguration serverConfiguration = new BasicConfiguration.ServerConfiguration();
        serverConfiguration.setAddress(getHost());
        serverConfiguration.setPort(getPort());

        return serverConfiguration;
    }

    public String getName() {
        return name;
    }

    public URL getUrl() {
        return url;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public SupportProduct getProduct() {
        return product;
    }

    public BasicConfiguration resolveConfiguration() {
        switch (this.product) {
            case RABBITMQ: return resolveRabbitmqConfiguration();
            case KAFKA: return resolveKafkaConfiguration();
            default:throw new NoSupportedMQException(this.product.getProtocol());
        }
    }

    protected RabbitmqConfiguration resolveRabbitmqConfiguration() {
        RabbitmqConfiguration configuration = new RabbitmqConfiguration();
        final String vhostKey = "vhost";
        final String usernameKey = "username";
        final String passwordKey = "password";

        String vhost = this.params.get(vhostKey);
        String username = this.params.get(usernameKey);
        String password = this.params.get(passwordKey);

        configuration.setServerList(List.of(getServer()));
        configuration.setVhost(vhost);
        configuration.setQueueName(this.path);
        configuration.setUsername(username);
        configuration.setPassword(password);

        return configuration;
    }

    protected KafkaConfiguration resolveKafkaConfiguration() {
        KafkaConfiguration configuration = new KafkaConfiguration();

        final String partitionClassKey = "partitionClass";
        String partitionClass = this.params.get(partitionClassKey);

        Class<? extends Partitioner> partitionerClass = null;
        try {
            partitionerClass = (Class<? extends Partitioner>) Class.forName(partitionClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        configuration.setTopic(this.path);
        //todo boostrap server?
        configuration.setServerList(List.of(getServer()));
        configuration.setPartitionerClass(partitionerClass);

        return configuration;
    }
}
