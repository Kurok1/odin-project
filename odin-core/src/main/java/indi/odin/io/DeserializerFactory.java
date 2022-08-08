package indi.odin.io;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 反序列化工厂
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class DeserializerFactory {

    private static final ConcurrentMap<String, Deserializer<?>> deserializerStore = new ConcurrentHashMap<>();

    public static final DeserializerFactory INSTANCE = new DeserializerFactory();

    private DeserializerFactory () {
        initializeDefaultDeserializers();
        initializerDiscoveryDeserializers();
    }

    private void initializeDefaultDeserializers() {
        deserializerStore.put(JvmDeserializer.class.getName(), new JvmDeserializer());
        deserializerStore.put(StringDeserializer.class.getName(), new StringDeserializer());
    }

    private void initializerDiscoveryDeserializers() {
        //load from property
        String deserializers = System.getProperty("odin.deserializers", "");
        if (!deserializers.isEmpty()) {
            for (String deserializer : deserializers.split(",")) {
                try {
                    Deserializer instance = (Deserializer) Class.forName(deserializer).getDeclaredConstructor().newInstance();
                    registerDeserializer(instance);
                } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
                         IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }


        //load from spi
        ServiceLoader<Deserializer> serviceLoader = ServiceLoader.load(Deserializer.class);
        for (Deserializer deserializer : serviceLoader) {
            registerDeserializer(deserializer);
        }
    }

    public void registerDeserializer(Deserializer<?> deserializer) {
        Objects.requireNonNull(deserializer);

        deserializerStore.putIfAbsent(deserializer.getClass().getName(), deserializer);
    }

    public Deserializer<?> retrieveDeserializer(String name) {
        return deserializerStore.get(name);
    }

    public Deserializer<?> retrieveOrDefault(String name) {
        return deserializerStore.getOrDefault(name, deserializerStore.get(JvmDeserializer.class.getName()));
    }

}
