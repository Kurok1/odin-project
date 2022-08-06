package indi.odin.sample.kafka;

import indi.odin.ConnectionSource;
import indi.odin.Message;
import indi.odin.client.Callback;
import indi.odin.client.Channel;
import indi.odin.client.kafka.KafkaChannelFactory;
import indi.odin.client.kafka.KafkaConfiguration;
import indi.odin.client.kafka.KafkaMessageAssembler;
import indi.odin.io.Serializers;

/**
 * TODO
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.2.0
 */
public class Producer {

    private static final String connectionStr = "kafka://localhost:9092/mytopic?partitionClass=org.apache.kafka.clients.producer.internals.DefaultPartitioner";

    public static void main(String[] args) throws Exception {
        ConnectionSource source = new ConnectionSource("test-kafka", connectionStr);
        KafkaConfiguration kafkaConfiguration = (KafkaConfiguration) source.resolveConfiguration();
        Serializers serializers = new Serializers();
        KafkaMessageAssembler assembler = new KafkaMessageAssembler();
        KafkaChannelFactory channelFactory = new KafkaChannelFactory(serializers, assembler);

        Channel channel = channelFactory.create("mytopic", kafkaConfiguration);

        channel.sendMessage("Hello, Kafka", new Callback() {
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
