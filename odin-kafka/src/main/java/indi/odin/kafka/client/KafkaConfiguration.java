package indi.odin.kafka.client;

import indi.odin.client.BasicConfiguration;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.clients.producer.internals.DefaultPartitioner;

import java.util.LinkedList;
import java.util.List;

/**
 * Kafka配置管理
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.3.0
 */
public class KafkaConfiguration extends BasicConfiguration {

    private String boostrapServerList;
    private String topic;
    private Class<? extends Partitioner> partitionerClass = DefaultPartitioner.class;

    private String groupId;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Class<? extends Partitioner> getPartitionerClass() {
        return partitionerClass;
    }

    public void setPartitionerClass(Class<? extends Partitioner> partitionerClass) {
        this.partitionerClass = partitionerClass;
    }

    public void setBoostrapServerList(String boostrapServerList) {
        this.boostrapServerList = boostrapServerList;
    }

    public String getBootstrapServerList() {
        if (boostrapServerList != null)
            return boostrapServerList;

        List<ServerConfiguration> serverConfigurations = getServerList();

        if (serverConfigurations == null || serverConfigurations.isEmpty())
            return "";

        List<String> serverList = new LinkedList<>();
        for (ServerConfiguration serverConfiguration : serverConfigurations) {
            serverList.add(String.format("%s:%d", serverConfiguration.getAddress(), serverConfiguration.getPort()));
        }
        this.boostrapServerList = String.join(",", serverList);
        return boostrapServerList;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
