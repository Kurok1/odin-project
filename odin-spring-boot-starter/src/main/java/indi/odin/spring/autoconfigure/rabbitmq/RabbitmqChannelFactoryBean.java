package indi.odin.spring.autoconfigure.rabbitmq;

import indi.odin.ConnectionPool;
import indi.odin.client.Channel;
import indi.odin.client.ChannelManager;
import indi.odin.client.rabbitmq.RabbitmqChannelFactory;
import indi.odin.client.rabbitmq.RabbitmqConfiguration;
import indi.odin.client.rabbitmq.RabbitmqMessageAssembler;
import indi.odin.spring.autoconfigure.OdinProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;

/**
 * rabbit管道创建
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 * @see FactoryBean
 */
public class RabbitmqChannelFactoryBean extends RabbitmqChannelFactory implements BeanFactoryAware, InitializingBean {

    private final static Logger logger = LoggerFactory.getLogger(RabbitmqChannelFactoryBean.class);

    private OdinProperty property;
    private ChannelManager channelManager;
    private ConnectionPool connectionPool;
    private ConfigurableListableBeanFactory beanFactory;

    public RabbitmqChannelFactoryBean(RabbitmqMessageAssembler rabbitmqMessageAssembler) {
        super(rabbitmqMessageAssembler);
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.property = beanFactory.getBeanProvider(OdinProperty.class).getIfUnique();
        this.channelManager = beanFactory.getBeanProvider(ChannelManager.class).getIfUnique();
        this.connectionPool = beanFactory.getBeanProvider(ConnectionPool.class).getIfUnique();
        this.beanFactory = (ConfigurableListableBeanFactory)beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //构建管道
        if (!CollectionUtils.isEmpty(property.getRabbitMq())) {
            for (OdinProperty.RabbitMq node : property.getRabbitMq()) {
                String beanName = node.getName();
                Channel channel = createChannel(node);
                this.beanFactory.registerSingleton(beanName, channel);
                this.channelManager.declareChannel(channel);
                logger.info("registered rabbitmq channel with name [{}]", beanName);
            }
        }
    }

    protected Channel createChannel(OdinProperty.RabbitMq node) throws IOException {
        RabbitmqConfiguration configuration = (RabbitmqConfiguration) connectionPool.getSource(node.getConnectionSource()).resolveConfiguration();

        return super.create(node.getName(), configuration);
    }
}
