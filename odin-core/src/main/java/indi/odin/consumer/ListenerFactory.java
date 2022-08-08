package indi.odin.consumer;

import indi.odin.SupportProduct;
import indi.odin.client.BasicConfiguration;
import indi.odin.exception.NoSupportedMQException;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 监听这创建工厂
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class ListenerFactory {

    private static final String MAPPING_FILE = "META-INF/odin-mappings.properties";
    private static final String MAPPING_PREFIX = "odin.protocol.";
    private static final Map<SupportProduct, Class<? extends Listener>> listenerMap = new HashMap<>();
    private static void loadProduct() {
        //get resource
        ClassLoader classLoader = ListenerFactory.class.getClassLoader();
        //load properties
        Properties properties = new Properties();
        try {
            Enumeration<URL> urls = classLoader.getResources(MAPPING_FILE);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                properties.load(url.openStream());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //key to product,value to listener class
        properties.forEach((key, value) -> {
            try {
                SupportProduct product = SupportProduct.read(key.toString().replace(MAPPING_PREFIX, ""));
                Class<? extends Listener> clazz = (Class<? extends Listener>) Class.forName(value.toString());
                listenerMap.putIfAbsent(product, clazz);
            } catch (NoSupportedMQException | ClassNotFoundException | ClassCastException ignored) {

            }
        });

    }
    static {
        loadProduct();
    }


    public static Listener createListener(SupportProduct supportProduct, BasicConfiguration configuration) throws Exception {
        if (!listenerMap.containsKey(supportProduct))
            throw new NoSupportedMQException(supportProduct.getProtocol());

        Class<? extends Listener> listenerClass = listenerMap.get(supportProduct);
        return listenerClass.getConstructor(BasicConfiguration.class).newInstance(configuration);
    }

}
