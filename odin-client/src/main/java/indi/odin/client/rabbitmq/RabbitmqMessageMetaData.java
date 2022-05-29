package indi.odin.client.rabbitmq;

import com.rabbitmq.client.AMQP;
import indi.odin.MessageMetaData;

/**
 * rabbitmq消息元数据
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 * @see com.rabbitmq.client.Envelope
 * @see com.rabbitmq.client.AMQP.BasicProperties
 */
public class RabbitmqMessageMetaData extends MessageMetaData {
    private long deliveryTag;
    private boolean redeliver;
    private String exchange;
    private String routingKey;

    private AMQP.BasicProperties basicProperties;

    public long getDeliveryTag() {
        return deliveryTag;
    }

    public void setDeliveryTag(long deliveryTag) {
        this.deliveryTag = deliveryTag;
    }

    public boolean isRedeliver() {
        return redeliver;
    }

    public void setRedeliver(boolean redeliver) {
        this.redeliver = redeliver;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public AMQP.BasicProperties getBasicProperties() {
        return basicProperties;
    }

    public void setBasicProperties(AMQP.BasicProperties basicProperties) {
        this.basicProperties = basicProperties;
    }
}
