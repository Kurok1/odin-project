package indi.odin.exception;

import indi.odin.consumer.Listener;
import indi.odin.consumer.MessageProcessor;

/**
 * 管道绑定消费者失败
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class BindFailedException extends RuntimeException {

    private static final String errorTemplate = "the messageProcessor [%s] bind the queueName [%s] failed, but has bind message processor is %s";

    public BindFailedException(Class<?> messageProcessorClass,
                               String queueName,
                               Class<?> hasBindMessageProcessor) {
        super(String.format(errorTemplate, messageProcessorClass.getName(), queueName, hasBindMessageProcessor.getName()));
    }
}
