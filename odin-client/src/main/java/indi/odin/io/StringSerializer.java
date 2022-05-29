package indi.odin.io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 字符串编码
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class StringSerializer implements Serializer {

    @Override
    public boolean support(Class<?> clazz, Object data) {
        return data instanceof String;
    }

    @Override
    public byte[] encode(Object data) throws IOException {
        return ((String) data).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public int getOrder() {
        return 100;
    }
}
