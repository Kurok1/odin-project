package indi.odin.client.rabbitmq;

import indi.odin.client.MessageAssembler;
import indi.odin.MessageMetaData;
import indi.odin.io.Serializer;
import indi.odin.io.Serializers;

import java.io.IOException;
import java.io.Serializable;

/**
 * rabbitmq消息加工
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 * @see indi.odin.client.MessageAssembler
 */
public class RabbitmqMessageAssembler implements MessageAssembler<RabbitmqMessage<?>> {

    private final Serializers serializers;

    public RabbitmqMessageAssembler(Serializers serializers) {
        this.serializers = serializers;
    }

    @Override
    public <E extends Serializable> RabbitmqMessage<?> mapping(E data, MessageMetaData metaData) throws IOException {
        if (metaData == null)
            throw new NullPointerException();
        RabbitmqMessageMetaData rabbitmqMessageMetaData = (RabbitmqMessageMetaData) metaData;
        Serializer<E> serializer = this.serializers.findOne(data);
        byte[] source = serializer.encode(data);
        rabbitmqMessageMetaData.getBasicProperties().getHeaders().put(Serializer.DESERIALIZER_CLASS_CONFIG_KEY, serializer.mappingDeserializerClass().getName());
        return new RabbitmqMessage<>(data, source, rabbitmqMessageMetaData);
    }
}
