package indi.odin.spring.autoconfigure;

import indi.odin.ConnectionPool;
import indi.odin.ConnectionSource;
import indi.odin.OdinURLStreamHandler;
import indi.odin.client.BasicConfiguration;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.util.CollectionUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 连接器注册
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.3.0
 */
public abstract class ConnectionRegister<C extends BasicConfiguration, S extends ConnectionSource<C>> implements BeanFactoryAware, InitializingBean {

    private ConfigurableBeanFactory beanFactory;
    private OdinProperty odinProperty;
    private Map<String, URL> urlMap = new HashMap<>();

    private final String BEAN_NAME_PREFIX = "odinConnection@";

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = (ConfigurableBeanFactory) beanFactory;
        odinProperty = this.beanFactory.getBeanProvider(OdinProperty.class).getIfUnique();
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        List<OdinProperty.ConnectionProperty> connectionPropertyList = this.odinProperty.getConnectionProperties();

        if (!CollectionUtils.isEmpty(connectionPropertyList)) {
            for (OdinProperty.ConnectionProperty connectionProperty : connectionPropertyList) {
                try {
                    URL url = new URL(null, connectionProperty.getUrl(), new OdinURLStreamHandler());
                    urlMap.put(connectionProperty.getName(), url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    throw new BeanInitializationException(e.getMessage());
                }
            }
        }

        Collection<S> sources = exposeConnections(this.beanFactory);
        sources.forEach(source -> {
            String beanName = BEAN_NAME_PREFIX + source.getName();
            this.beanFactory.registerSingleton(beanName, source);
            ConnectionPool.INSTANCE.registerConnection(source);
        });
    }

    protected Collection<S> exposeConnections(BeanFactory beanFactory) {
        return getUrlMap().entrySet().stream()
                .filter(entry -> getSupportProductProtocol().equals(entry.getValue().getProtocol()))
                .map(entry -> toConnectionSource(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    protected abstract String getSupportProductProtocol();

    protected abstract S toConnectionSource(String name, URL url);

    protected OdinProperty getOdinProperty() {
        return this.odinProperty;
    }

    protected Map<String, URL> getUrlMap() {
        return this.urlMap;
    }

}
