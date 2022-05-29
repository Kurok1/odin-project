package indi.odin.spring.consumer;

import indi.odin.ConnectionPool;
import indi.odin.ConnectionSource;
import indi.odin.SupportProduct;
import indi.odin.client.BasicConfiguration;
import indi.odin.consumer.Listener;
import indi.odin.consumer.ListenerExecutor;
import indi.odin.consumer.ListenerFactory;
import indi.odin.consumer.MessageProcessor;
import indi.odin.spring.autoconfigure.OdinProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 消息监听处理，注册到{@link indi.odin.consumer.ListenerExecutor}
 *
 * @author <a href="mailto:hanchao@66yunlian.com">韩超</a>
 * @since 1.0.0
 */
public class MessageListenerPostProcessor implements BeanPostProcessor, BeanFactoryAware {

    private ListenerExecutor listenerExecutor = ListenerExecutor.INSTANCE;
    private OdinProperty property;

    private static final Logger logger = LoggerFactory.getLogger(MessageListenerPostProcessor.class);

    private boolean isMessageListener(Object bean) {
        return bean instanceof MessageProcessor && bean.getClass().isAnnotationPresent(MessageListener.class);
    }

    protected MessageListener retrieveMessageListener(Object bean) {
        return bean.getClass().getDeclaredAnnotation(MessageListener.class);
    }

    private final ConnectionPool connectionPool;

    public MessageListenerPostProcessor(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (property == null)
            return bean;
        if (isMessageListener(bean)) {
            MessageListener messageListener = retrieveMessageListener(bean);

            final ConnectionSource connectionSource = this.connectionPool.getSource(messageListener.connectionKey());

            final BasicConfiguration basicConfiguration = connectionSource.resolveConfiguration();
            final SupportProduct product = connectionSource.getProduct();

            try {
                Listener listener = ListenerFactory.createListener(product, basicConfiguration);

                String[] bindingQueue = messageListener.bindingQueue();
                listener.bindQueues(bindingQueue);
                for (String queue : bindingQueue) {
                    listener.bindMessageProcessor(queue, (MessageProcessor<?>) bean);
                    logger.info("bind queue [{}] with message processor [{}]", queue, bean.getClass().getName());
                }

                this.listenerExecutor.addListener(listener);
            } catch (Exception e) {
                e.printStackTrace();
                throw new BeanCreationException(e.getMessage());
            }
        }

        return bean;

    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.property = beanFactory.getBean(OdinProperty.class);
    }
}
