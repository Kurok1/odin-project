package indi.odin.client;


import indi.odin.exception.ChannelClosedException;

import java.io.Closeable;
import java.io.IOException;
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

    void sendMessage(Object message) throws IOException;

    void sendMessage(Object message, Properties headers) throws IOException;

    void sendMessage(Object message, Callback callback) throws IOException;

    void sendMessage(Object message, Properties propheaderserties, Callback callback) throws IOException;

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
