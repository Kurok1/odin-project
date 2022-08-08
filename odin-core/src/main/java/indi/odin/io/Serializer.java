package indi.odin.io;

import java.io.IOException;
import java.util.Comparator;

/**
 * 序列化器
 *
 * @author <a href="mailto:hanchao@66yunlian.com">韩超</a>
 * @since 1.0.0
 */
public interface Serializer<T> extends Ordered {

    boolean support(Class<?> clazz, Object data);

    byte[] encode(T data) throws IOException;

    Class<? extends Deserializer<?>> mappingDeserializerClass();

    String DESERIALIZER_CLASS_CONFIG_KEY = "odin.deserializer.class.name";

}
