package indi.odin.consumer.rabbitmq;

import indi.odin.client.rabbitmq.RabbitmqMessage;
import indi.odin.consumer.MessageProcessor;

/**
 * Rabbitmq消息处理
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public interface RabbitmqMessageProcessor extends MessageProcessor<RabbitmqMessage> {

}
