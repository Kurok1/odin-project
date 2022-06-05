package indi.odin.io;

import java.io.IOException;
import java.io.Serializable;

/**
 * 反序列化
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public interface Deserializer<T> {

    T decode(byte[] bytes) throws IOException;

}
