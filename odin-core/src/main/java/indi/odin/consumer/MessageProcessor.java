package indi.odin.consumer;

import indi.odin.Message;

import java.util.UUID;

/**
 * 消息处理
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
@FunctionalInterface
public interface MessageProcessor<M extends Message> {

    default String getId() {
        return UUID.randomUUID().toString();
    }

    HandleResponse onMessage(String queueName, M message);

}
