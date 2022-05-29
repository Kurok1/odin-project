package indi.odin.client.rabbitmq;

import indi.odin.Message;

/**
 * rabbitmq消息
 *
 * @author <a href="mailto:hanchao@66yunlian.com">韩超</a>
 * @since 1.0.0
 */
public class RabbitmqMessage implements Message {

    private final byte[] source;

    private final RabbitmqMessageMetaData metaData;

    public RabbitmqMessage(byte[] source, RabbitmqMessageMetaData metaData) {
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
}
