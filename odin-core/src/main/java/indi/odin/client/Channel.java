package indi.odin.client;


import indi.odin.exception.ChannelClosedException;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

/**
 * 消息管道，消息发送方与具体的消息队列绑定
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public interface Channel extends Closeable {

    String name();

    String queueName();

    void setDefaultCallback(Callback callback);

    <T extends Serializable> void sendMessage(T message) throws IOException;

    <T extends Serializable> void sendMessage(T message, Properties headers) throws IOException;

    <T extends Serializable> void sendMessage(T message, Callback callback) throws IOException;

    <T extends Serializable> void sendMessage(T message, Properties properties, Callback callback) throws IOException;

    boolean isOpen();

    /**
     * 检查是否关闭
     * @throws ChannelClosedException 关闭时触发
     */
    default void checkClosed() {
        if (!isOpen())
            throw new ChannelClosedException(name());
    }

}
