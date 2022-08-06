package indi.odin.sample.spring.kafka;

import indi.odin.client.kafka.KafkaMessage;
import indi.odin.client.rabbitmq.RabbitmqMessage;
import indi.odin.consumer.HandleResponse;
import indi.odin.consumer.MessageProcessor;
import indi.odin.spring.consumer.MessageListener;

import java.nio.charset.StandardCharsets;

/**
 * TODO
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.2.0
 */
@MessageListener(bindingQueue = {"mytopic"}, connectionKey = "test-kafka-consumer")
public class KafkaTestConsumer implements MessageProcessor<KafkaMessage<String>> {

    @Override
    public HandleResponse onMessage(String queueName, KafkaMessage<String> message) {
        String str = message.getSource();
        System.out.println("current thread is " + Thread.currentThread().getName());
        System.out.printf("received queue [%s] and body is [%s]", queueName, str);
        return HandleResponse.SUCCESS;
    }


}
