package indi.odin.sample.kafka;

import indi.odin.ConnectionSource;
import indi.odin.Message;
import indi.odin.client.Callback;
import indi.odin.client.Channel;
import indi.odin.io.Serializers;
import indi.odin.kafka.KafkaConnectionSource;
import indi.odin.kafka.client.KafkaChannelFactory;
import indi.odin.kafka.client.KafkaConfiguration;
import indi.odin.kafka.client.KafkaMessageAssembler;

/**
 * TODO
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.2.0
 */
public class Producer {

    private static final String connectionStr = "kafka://localhost:9092/mytopic?partitionClass=org.apache.kafka.clients.producer.internals.DefaultPartitioner";

    public static void main(String[] args) throws Exception {
        ConnectionSource<KafkaConfiguration> source = new KafkaConnectionSource("test-kafka", connectionStr);
        KafkaConfiguration kafkaConfiguration = (KafkaConfiguration) source.resolveConfiguration();
        Serializers serializers = new Serializers();
        KafkaMessageAssembler assembler = new KafkaMessageAssembler();
        KafkaChannelFactory channelFactory = new KafkaChannelFactory(serializers, assembler);

        Channel channel = channelFactory.create("mytopic", kafkaConfiguration);

        channel.sendMessage("Hello, Kafka V1.3.0", new Callback() {
            @Override
            public void onSuccess(Message message, Channel channel) {
                System.out.println("send ok");
            }

            @Override
            public void onFailure(Message message, Channel channel, String s) {
                System.out.println("send fail");
            }
        });
    }

}
