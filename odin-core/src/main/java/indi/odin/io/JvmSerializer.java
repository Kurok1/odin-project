package indi.odin.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * TODO
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class JvmSerializer implements Serializer<Object> {

    @Override
    public boolean support(Class<?> clazz, Object data) {
        return data instanceof Serializable;
    }

    @Override
    public byte[] encode(Object data) throws IOException {
        try (
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)
                ) {
            objectOutputStream.writeObject(data);
            return outputStream.toByteArray();
        }
    }

    @Override
    public Class<? extends Deserializer<?>> mappingDeserializerClass() {
        return JvmDeserializer.class;

    }
}
