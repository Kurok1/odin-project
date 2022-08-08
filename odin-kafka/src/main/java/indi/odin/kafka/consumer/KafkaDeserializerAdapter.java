package indi.odin.kafka.consumer;

import indi.odin.io.DeserializerFactory;
import indi.odin.io.JvmDeserializer;
import indi.odin.io.Serializer;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * kafka 反序列化适配
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.3.0
 */
public class KafkaDeserializerAdapter implements Deserializer<Object> {

    private DeserializerFactory deserializerFactory = DeserializerFactory.INSTANCE;

    @Override
    public Object deserialize(String topic, byte[] data) {
        return this.deserialize(topic, null , data);
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Deserializer.super.configure(configs, isKey);
    }

    @Override
    public Object deserialize(String topic, Headers headers, byte[] data) {
        String deserializerClassName = null;
        Header header = headers.lastHeader(Serializer.DESERIALIZER_CLASS_CONFIG_KEY);
        if (header == null)
            deserializerClassName = JvmDeserializer.class.getName();
        else deserializerClassName = new String(header.value(), StandardCharsets.UTF_8);


        indi.odin.io.Deserializer deserializer = this.deserializerFactory.retrieveOrDefault(deserializerClassName);
        try {
            return deserializer.decode(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        Deserializer.super.close();
    }
}
