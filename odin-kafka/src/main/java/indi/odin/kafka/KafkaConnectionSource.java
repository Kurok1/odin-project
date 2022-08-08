package indi.odin.kafka;

import indi.odin.ConnectionSource;
import indi.odin.OdinURLStreamHandler;
import indi.odin.kafka.client.KafkaConfiguration;
import org.apache.kafka.clients.producer.Partitioner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * kakfa connection
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.3.0
 */
public class KafkaConnectionSource extends ConnectionSource<KafkaConfiguration> {

    public KafkaConnectionSource(String name, String url) throws MalformedURLException {
        this(name, new URL(null, url, new OdinURLStreamHandler()));
    }

    public KafkaConnectionSource(String name, URL url) {
        super(name, url);
    }

    @Override
    public KafkaConfiguration resolveConfiguration() {
        KafkaConfiguration configuration = new KafkaConfiguration();

        final String partitionClassKey = "partitionClass";
        String partitionClass = this.params.get(partitionClassKey);

        if (partitionClass == null || partitionClass.isEmpty())
            partitionClass = "org.apache.kafka.clients.producer.internals.DefaultPartitioner";

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
        configuration.setGroupId(this.params.get("groupId"));

        return configuration;
    }
}
