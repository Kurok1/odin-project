package indi.odin.client.rabbitmq;

import indi.odin.client.MessageAssembler;
import indi.odin.MessageMetaData;
import indi.odin.io.Serializers;

import java.io.IOException;

/**
 * rabbitmq消息加工
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 * @see indi.odin.client.MessageAssembler
 */
public class RabbitmqMessageAssembler implements MessageAssembler<RabbitmqMessage> {

    private final Serializers serializers;

    public RabbitmqMessageAssembler(Serializers serializers) {
        this.serializers = serializers;
    }

    @Override
    public RabbitmqMessage mapping(Object data, MessageMetaData metaData) throws IOException {
        if (metaData == null)
            throw new NullPointerException();
        RabbitmqMessageMetaData rabbitmqMessageMetaData = (RabbitmqMessageMetaData) metaData;
        byte[] source = this.serializers.encode(data);
        return new RabbitmqMessage(source, rabbitmqMessageMetaData);
    }
}
