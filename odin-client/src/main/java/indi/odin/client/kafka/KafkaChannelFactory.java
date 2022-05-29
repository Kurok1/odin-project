package indi.odin.client.kafka;

import indi.odin.client.ChannelFactory;
import indi.odin.io.Serializers;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.io.IOException;
import java.util.Properties;

/**
 * Kafka管道工厂
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class KafkaChannelFactory implements ChannelFactory<KafkaChannel, KafkaConfiguration> {

    private final KafkaMessageAssembler assembler;
    private final Serializers serializers;

    public KafkaChannelFactory(Serializers serializers, KafkaMessageAssembler assembler) {
        this.serializers = serializers;
        this.assembler = assembler;
    }

    @Override
    public KafkaChannel create(String name, KafkaConfiguration configuration) throws IOException {
        Properties properties = new Properties();
        properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, configuration.getBootstrapServerList());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaSerializerAdapter.class.getName());
        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, configuration.getPartitionerClass().getName());
        properties.put(KafkaSerializerAdapter.SERIALIZER_INSTANCE, serializers);

        KafkaProducer<String, Object> kafkaProducer = new KafkaProducer<>(properties);

        return new KafkaChannel(kafkaProducer, name, configuration.getTopic(), this.assembler);
    }
}
