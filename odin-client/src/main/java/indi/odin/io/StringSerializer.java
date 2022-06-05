package indi.odin.io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 字符串编码
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class StringSerializer implements Serializer<String> {

    @Override
    public boolean support(Class<?> clazz, Object data) {
        return data instanceof String;
    }

    @Override
    public byte[] encode(String data) throws IOException {
        return data.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public Class<? extends Deserializer<?>> mappingDeserializerClass() {
        return StringDeserializer.class;
    }

    @Override
    public int getOrder() {
        return 100;
    }
}
