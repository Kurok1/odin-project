package indi.odin.sample.spring;

import indi.odin.client.rabbitmq.RabbitmqMessage;
import indi.odin.consumer.HandleResponse;
import indi.odin.consumer.MessageProcessor;
import indi.odin.spring.consumer.MessageListener;

import java.nio.charset.StandardCharsets;

/**
 * TODO
 *
 * @author <a href="mailto:hanchao@66yunlian.com">韩超</a>
 * @since 1.0.0
 */
@MessageListener(bindingQueue = {"test-odin"}, connectionKey = "test-consumer")
public class TestConsumer implements MessageProcessor<RabbitmqMessage> {

    @Override
    public HandleResponse onMessage(String queueName, RabbitmqMessage message) {
        String str = new String(message.getSources(), StandardCharsets.UTF_8);
        System.out.println("current thread is " + Thread.currentThread().getName());
        System.out.printf("received queue [%s] and body is [%s]", queueName, str);
        return HandleResponse.SUCCESS;
    }


}
