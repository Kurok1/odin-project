package indi.odin.spring.kafka;

import indi.odin.ConnectionPool;
import indi.odin.client.Channel;
import indi.odin.client.ChannelManager;
import indi.odin.io.Serializers;
import indi.odin.kafka.client.KafkaChannelFactory;
import indi.odin.kafka.client.KafkaConfiguration;
import indi.odin.kafka.client.KafkaMessageAssembler;
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
 * kakfa管道创建
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 * @see FactoryBean
 */
public class KafkaChannelFactoryBean extends KafkaChannelFactory implements InitializingBean, BeanFactoryAware {

    private static final Logger logger = LoggerFactory.getLogger(KafkaChannelFactoryBean.class);

    private OdinProperty property;
    private ChannelManager channelManager;
    private ConnectionPool connectionPool;
    private ConfigurableListableBeanFactory beanFactory;

    public KafkaChannelFactoryBean(Serializers serializers, KafkaMessageAssembler kafkaMessageAssembler) {
        super(serializers, kafkaMessageAssembler);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.property = beanFactory.getBeanProvider(OdinProperty.class).getIfUnique();
        this.channelManager = beanFactory.getBeanProvider(ChannelManager.class).getIfUnique();
        this.connectionPool = ConnectionPool.INSTANCE;
        this.beanFactory = (ConfigurableListableBeanFactory)beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //构建管道
        if (!CollectionUtils.isEmpty(property.getKafka())) {
            for (OdinProperty.Kafka node : property.getKafka()) {
                String beanName = node.getName();
                Channel channel = createChannel(node);
                this.beanFactory.registerSingleton(beanName, channel);
                this.channelManager.declareChannel(channel);
                logger.info("register kafka channel with name [{}]", beanName);
            }
        }
    }

    protected Channel createChannel(OdinProperty.Kafka node) throws IOException {
        KafkaConfiguration configuration = (KafkaConfiguration) connectionPool.getSource(node.getConnectionSource()).resolveConfiguration();

        return super.create(node.getName(), configuration);
    }

}
