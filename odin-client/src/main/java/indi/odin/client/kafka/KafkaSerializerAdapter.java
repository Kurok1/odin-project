package indi.odin.client.kafka;

import indi.odin.io.Serializers;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.io.IOException;
import java.util.Map;

/**
 * 序列化适配
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class KafkaSerializerAdapter implements Serializer<Object> {

    public static final String SERIALIZER_INSTANCE = "serializer.instance";

    private Serializers serializers;

    @Override
    public byte[] serialize(String topic, Object data) {
        try {
            return this.serializers.encode(data);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        this.serializers = (Serializers) configs.get(SERIALIZER_INSTANCE);
    }

    @Override
    public byte[] serialize(String topic, Headers headers, Object data) {
        return serialize(topic, data);
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}
