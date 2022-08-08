package indi.odin.io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 字符串反序列化，UTF-8
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class StringDeserializer implements Deserializer<String> {

    @Override
    public String decode(byte[] bytes) throws IOException {
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
