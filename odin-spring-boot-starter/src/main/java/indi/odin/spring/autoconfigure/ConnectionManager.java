package indi.odin.spring.autoconfigure;

import indi.odin.ConnectionPool;
import indi.odin.ConnectionSource;
import indi.odin.OdinURLStreamHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * TODO
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class ConnectionManager implements FactoryBean<ConnectionPool>, DisposableBean, BeanFactoryAware {

    private final ConnectionPool pool = ConnectionPool.INSTANCE;

    @Override
    public ConnectionPool getObject() throws Exception {
        return this.pool;
    }

    @Override
    public Class<?> getObjectType() {
        return ConnectionPool.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        OdinProperty property = beanFactory.getBeanProvider(OdinProperty.class).getIfUnique();
        List<OdinProperty.ConnectionProperty> connectionPropertyList = property.getConnectionProperties();

        if (!CollectionUtils.isEmpty(connectionPropertyList)) {
            for (OdinProperty.ConnectionProperty connectionProperty : connectionPropertyList) {
                try {
                    ConnectionSource connectionSource = createConnectionSource(connectionProperty);
                    this.pool.registerConnection(connectionSource);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    throw new BeanInitializationException(e.getMessage());
                }
            }
        }
    }

    protected ConnectionSource createConnectionSource(OdinProperty.ConnectionProperty connectionProperty) throws MalformedURLException {
        Assert.notNull(connectionProperty.getName(), "name cannot be null");
        Assert.notNull(connectionProperty.getUrl(), "url cannot be null");
        URL url = new URL(null, connectionProperty.getUrl(), new OdinURLStreamHandler());
        return new ConnectionSource(connectionProperty.getName(), url);
    }

    @Override
    public void destroy() throws Exception {
        this.pool.clear();
    }
}
