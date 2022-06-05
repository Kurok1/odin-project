package indi.odin.client.rabbitmq;

import indi.odin.Message;

import java.io.Serializable;

/**
 * rabbitmq消息
 *
 * @author <a href="mailto:hanchao@66yunlian.com">韩超</a>
 * @since 1.0.0
 */
public class RabbitmqMessage<T> implements Message<T> {

    private final byte[] source;

    private final RabbitmqMessageMetaData metaData;

    private Object target;

    public RabbitmqMessage(T target, byte[] source, RabbitmqMessageMetaData metaData) {
        this.target = target;
        this.source = source;
        this.metaData = metaData;
    }

    @Override
    public byte[] getSources() {
        return source;
    }

    @Override
    public RabbitmqMessageMetaData metaData() {
        return this.metaData;
    }

    public T getSource() {
        return (T)target;
    }

    public void setSource(Object target) {
        this.target = target;
    }
}
