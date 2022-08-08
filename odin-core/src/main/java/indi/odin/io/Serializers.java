package indi.odin.io;

import indi.odin.exception.NoSuitableSerializerException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 序列化器集合
 *
 * @author <a href="mailto:hanchao@66yunlian.com">韩超</a>
 * @since 1.0.0
 */
public class Serializers {

    private List<Serializer<?>> serializers = new CopyOnWriteArrayList<>();

    public Serializers(Collection<Serializer<?>> serializers) {
        this.serializers.addAll(serializers);
        initializeDefaultSerializers();
        initializeDiscoveredSerializers();
        sort();
    }

    public Serializers() {
        initializeDefaultSerializers();
        initializeDiscoveredSerializers();
        sort();
    }

    private void sort() {
        if (!serializers.isEmpty()) {
            serializers.sort(Ordered.OrderComparator.INSTANCE);
        }
    }

    private void initializeDefaultSerializers() {
        //集成自带
        serializers.add(new JvmSerializer());
        serializers.add(new StringSerializer());
    }

    protected void initializeDiscoveredSerializers() {
        //第三方加载的工具
    }

    public Serializer findOne(Object data) {
        for (Serializer serializer : serializers)
            if (serializer.support(data.getClass(), data))
                return serializer;

        return null;
    }

    public byte[] encode(Object data) throws IOException {
        Serializer serializer = findOne(data);

        if (serializer == null)
            throw new NoSuitableSerializerException();

        return serializer.encode(data);
    }



}
