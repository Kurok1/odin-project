package indi.odin.consumer.rabbitmq;

import com.rabbitmq.client.*;
import indi.odin.client.rabbitmq.RabbitmqMessage;
import indi.odin.client.rabbitmq.RabbitmqMessageMetaData;
import indi.odin.consumer.HandleResponse;
import indi.odin.consumer.MessageProcessor;
import indi.odin.io.Deserializer;
import indi.odin.io.DeserializerFactory;
import indi.odin.io.Serializer;

import java.io.IOException;
import java.util.Objects;

/**
 * TODO
 *
 * @author <a href="mailto:hanchao@66yunlian.com">韩超</a>
 * @since 1.0.0
 */
public class RabbitMqConsumer implements Consumer {

    private final MessageProcessor<RabbitmqMessage> messageProcessor;
    private final Channel channel;
    private final String queueName;

    public RabbitMqConsumer(String queueName, Channel channel, MessageProcessor<RabbitmqMessage> messageProcessor) {
        this.queueName = queueName;
        this.channel = channel;
        this.messageProcessor = messageProcessor;
    }

    @Override
    public void handleConsumeOk(String consumerTag) {
        //nothing to do
    }

    @Override
    public void handleCancelOk(String consumerTag) {
        //nothing to do
    }

    @Override
    public void handleCancel(String consumerTag) throws IOException {
        //nothing to do
    }

    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
        //nothing to do
    }

    @Override
    public void handleRecoverOk(String consumerTag) {
        //nothing to do
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        long deliveryTag = envelope.getDeliveryTag();
        RabbitmqMessageMetaData metaData = new RabbitmqMessageMetaData();
        metaData.setDeliveryTag(deliveryTag);
        metaData.setRoutingKey(envelope.getRoutingKey());
        metaData.setRedeliver(envelope.isRedeliver());
        metaData.setExchange(envelope.getExchange());
        metaData.setBasicProperties(properties);

        String deserializerClass = properties.getHeaders().get(Serializer.DESERIALIZER_CLASS_CONFIG_KEY).toString();

        Deserializer<?> deserializer = DeserializerFactory.INSTANCE.retrieveOrDefault(deserializerClass);

        Object target = deserializer.decode(body);

        RabbitmqMessage message = new RabbitmqMessage(target, body, metaData);

        HandleResponse response = this.messageProcessor.onMessage(this.queueName, message);

        if (response == HandleResponse.SUCCESS) {
            this.channel.basicAck(envelope.getDeliveryTag(), false);
        } else if (response == HandleResponse.NEED_RESEND) {
            this.channel.basicNack(envelope.getDeliveryTag(), false, true);
        } else if (response == HandleResponse.FAILURE) {
            this.channel.basicNack(envelope.getDeliveryTag(), false, false);
        }
    }
}
