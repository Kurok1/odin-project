package indi.odin.rabbitmq;

import indi.odin.ConnectionSource;
import indi.odin.OdinURLStreamHandler;
import indi.odin.rabbitmq.client.RabbitmqConfiguration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * rabbit connection source
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.3.0
 */
public class RabbitConnectionSource extends ConnectionSource<RabbitmqConfiguration> {

    public RabbitConnectionSource(String name, String url) throws MalformedURLException {
        this(name, new URL(null, url, new OdinURLStreamHandler()));
    }

    public RabbitConnectionSource(String name, URL url) {
        super(name, url);
    }

    @Override
    public RabbitmqConfiguration resolveConfiguration() {
        RabbitmqConfiguration configuration = new RabbitmqConfiguration();
        final String vhostKey = "vhost";
        final String usernameKey = "username";
        final String passwordKey = "password";

        String vhost = super.params.get(vhostKey);
        String username = this.params.get(usernameKey);
        String password = this.params.get(passwordKey);

        configuration.setServerList(List.of(getServer()));
        configuration.setVhost(vhost);
        configuration.setQueueName(this.path);
        configuration.setUsername(username);
        configuration.setPassword(password);

        return configuration;
    }
}
