package indi.odin.rabbitmq.client;

import com.rabbitmq.client.AMQP;
import indi.odin.MessageMetaData;
import indi.odin.client.MessageAssembler;
import indi.odin.io.Serializer;
import indi.odin.io.Serializers;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

/**
 * rabbitmq消息加工
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.3.0
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
        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
        builder.headers(Map.of(Serializer.DESERIALIZER_CLASS_CONFIG_KEY, serializer.mappingDeserializerClass().getName()));
        rabbitmqMessageMetaData.setBasicProperties(builder.build());
        return new RabbitmqMessage<>(data, source, rabbitmqMessageMetaData);
    }
}
