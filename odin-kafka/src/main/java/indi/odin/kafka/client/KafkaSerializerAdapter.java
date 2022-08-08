package indi.odin.kafka.client;

import indi.odin.exception.NoSuitableSerializerException;
import indi.odin.io.Serializers;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 序列化适配
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.3.0
 */
public class KafkaSerializerAdapter implements Serializer<Object> {

    public static final String SERIALIZER_INSTANCE = "serializer.instance";

    private Serializers serializers;

    @Override
    public byte[] serialize(String topic, Object data) {
        return this.serialize(topic, null, data);
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        this.serializers = (Serializers) configs.get(SERIALIZER_INSTANCE);
    }

    @Override
    public byte[] serialize(String topic, Headers headers, Object data) {
        if (data == null)
            return null;
        indi.odin.io.Serializer serializer = this.serializers.findOne(data);
        if (serializer == null)
            throw new NoSuitableSerializerException();
        if (headers != null)
            headers.add(indi.odin.io.Serializer.DESERIALIZER_CLASS_CONFIG_KEY, serializer.mappingDeserializerClass().getName().getBytes(StandardCharsets.UTF_8));

        try {
            return serializer.encode(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}
