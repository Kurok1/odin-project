package indi.odin.client.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import indi.odin.client.Callback;
import indi.odin.client.Channel;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

/**
 * Rabbitmq管道
 *
 * @author <a href="mailto:hanchao@66yunlian.com">韩超</a>
 * @since 1.0.0
 */
public class RabbitmqChannel implements Channel {

    private final com.rabbitmq.client.Channel delegateChannel;
    private final String name;
    private final String queueName;

    private final String exchangeName;
    private final String routingKey;

    private final RabbitmqMessageAssembler assembler;
    private Optional<Callback> defaultCallback = Optional.empty();

    public RabbitmqChannel(String name, String queueName, com.rabbitmq.client.Channel delegateChannel, RabbitmqMessageAssembler assembler) throws IOException {
        this.delegateChannel = delegateChannel;
        this.name = name;
        this.queueName = queueName;
        this.exchangeName = String.format("%s|%s", name, queueName);
        this.routingKey = "_route_direct_" + exchangeName;
        this.assembler = assembler;
        initialize();
    }

    /**
     * 初始化交换机等操作
     */
    protected void initialize() throws IOException {
        //直连交换机
        this.delegateChannel.exchangeDeclare(this.exchangeName, BuiltinExchangeType.DIRECT);
        this.delegateChannel.queueDeclare(this.queueName, false, false, false, Collections.emptyMap());
        //绑定队列
        this.delegateChannel.queueBind(this.queueName, this.exchangeName, this.routingKey);
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String queueName() {
        return this.queueName;
    }

    @Override
    public void setDefaultCallback(Callback callback) {
        if (callback == null)
            throw new NullPointerException();
        this.defaultCallback = Optional.of(callback);
        this.delegateChannel.clearReturnListeners();
        this.delegateChannel.addReturnListener(new ReturnCallbackAdapter(this, callback));
    }

    @Override
    public <T extends Serializable> void sendMessage(T message) throws IOException {
        sendMessage(message, new Properties());
    }

    @Override
    public <T extends Serializable> void sendMessage(T message, Properties headers) throws IOException {
        sendMessage(message, headers, this.defaultCallback.orElse(null));
    }

    @Override
    public <T extends Serializable> void sendMessage(T message, Callback callback) throws IOException {
        if (callback == null)
            throw new NullPointerException();
        sendMessage(message, new Properties(), callback);
    }

    @Override
    public <T extends Serializable> void sendMessage(T message, Properties headers, Callback callback) throws IOException {
        checkClosed();
        //解析元信息
        RabbitmqMessageMetaData metaData = new RabbitmqMessageMetaData();
        if (headers != null && headers.size() > 0) {
            headers.stringPropertyNames().forEach(
                    property -> metaData.getBasicProperties()
                            .getHeaders()
                            .put(property, headers.getProperty(property))
            );
        }
        RabbitmqMessage<?> rabbitmqMessage = this.assembler.mapping(message, metaData);
        try {
            this.delegateChannel.basicPublish(this.exchangeName, this.routingKey, rabbitmqMessage.metaData().getBasicProperties(), rabbitmqMessage.getSources());
            if (callback != null)
                callback.onSuccess(rabbitmqMessage, this);
        } catch (Exception e) {
            if (callback != null)
                callback.onFailure(rabbitmqMessage, this, e.getMessage());
        }
    }

    @Override
    public boolean isOpen() {
        return this.delegateChannel.isOpen();
    }

    @Override
    public void close() throws IOException {
        try {
            this.delegateChannel.queueUnbind(this.queueName, this.exchangeName, this.routingKey);
            this.delegateChannel.exchangeDelete(this.exchangeName, true);
            this.delegateChannel.queueDelete(this.queueName, true, true);
            this.delegateChannel.close();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
