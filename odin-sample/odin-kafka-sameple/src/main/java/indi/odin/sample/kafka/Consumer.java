package indi.odin.sample.kafka;

import indi.odin.ConnectionSource;
import indi.odin.Message;
import indi.odin.client.kafka.KafkaConfiguration;
import indi.odin.client.kafka.KafkaMessage;
import indi.odin.consumer.HandleResponse;
import indi.odin.consumer.Listener;
import indi.odin.consumer.ListenerFactory;
import indi.odin.consumer.MessageProcessor;

/**
 * TODO
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.2.0
 */
public class Consumer {

    private static final String connectionStr = "kafka://localhost:9092/mytopic?groupId=test&partitionClass=org.apache.kafka.clients.producer.internals.DefaultPartitioner";
    private static final String bind_topic = "mytopic";

    public static void main(String[] args) throws Exception {

        ConnectionSource connectionSource = new ConnectionSource("test-kafak", connectionStr);
        KafkaConfiguration kafkaConfiguration = (KafkaConfiguration) connectionSource.resolveConfiguration();
        Listener listener = ListenerFactory.createListener(connectionSource.getProduct(), kafkaConfiguration);

        listener.bindQueues(bind_topic);
        listener.bindMessageProcessor(bind_topic, (MessageProcessor<KafkaMessage<String>>) (s, stringKafkaMessage) -> {
            String message = stringKafkaMessage.getSource();
            System.out.printf("get message from kafka : %s\n", message);
            return HandleResponse.SUCCESS;
        });

        listener.start();

        listener.stop();

    }

}
