package indi.odin.client.kafka;

import indi.odin.client.Callback;
import indi.odin.client.Channel;
import indi.odin.exception.SendFailException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Kafka 管道
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class KafkaChannel implements Channel {

    public static final String PARTITION_KEY = "kafka.partition";

    private final KafkaProducer<String, Object> producer;
    private final String name;
    private final String queueName;
    private final KafkaMessageAssembler assembler;
    private Callback defaultCallback = null;

    private volatile boolean open = true;

    public KafkaChannel(KafkaProducer<String, Object> producer, String name, String queueName, KafkaMessageAssembler kafkaMessageAssembler) {
        this.producer = producer;
        this.name = name;
        this.queueName = queueName;
        this.assembler = kafkaMessageAssembler;
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
        this.defaultCallback = callback;
    }

    @Override
    public <T extends Serializable> void sendMessage(T message) throws IOException {
        sendMessage(message, null, this.defaultCallback);
    }

    @Override
    public <T extends Serializable> void sendMessage(T message, Properties headers) throws IOException {
        sendMessage(message, headers, this.defaultCallback);
    }

    @Override
    public <T extends Serializable> void sendMessage(T message, Callback callback) throws IOException {
        if (callback == null)
            throw new NullPointerException();

        sendMessage(message, null, callback);
    }

    @Override
    public <T extends Serializable> void sendMessage(T message, Properties headers, Callback callback) throws IOException {
        checkClosed();

        KafkaMetaData metaData = buildMetaData(headers);
        KafkaMessage<T> kafkaMessage = this.assembler.mapping(message, metaData);
        org.apache.kafka.clients.producer.Callback callbackAdapter = makeCallback(kafkaMessage, callback);

        Future<RecordMetadata> recordMetadataFuture = this.producer.send(kafkaMessage.convert(), callbackAdapter);

        try {
            //同步发送
            recordMetadataFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new SendFailException(e);
        }

    }

    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    public void close() throws IOException {
        synchronized (this) {
            this.producer.close();
            this.open = false;
        }
    }

    protected KafkaMetaData buildMetaData(Properties properties) {
        KafkaMetaData kafkaMetaData = new KafkaMetaData();
        kafkaMetaData.setQueueName(this.queueName);
        kafkaMetaData.setCreated(LocalDateTime.now());

        if (properties != null && !properties.isEmpty()) {
            Set<String> propertyNames = properties.stringPropertyNames();
            for (String propertyName : propertyNames)
                kafkaMetaData.setHeader(propertyName, properties.getProperty(propertyName));
        }

        return kafkaMetaData;
    }

    protected org.apache.kafka.clients.producer.Callback makeCallback(KafkaMessage kafkaMessage, Callback callback) {
        return new KafkaCallbackAdapter(callback, this, kafkaMessage);
    }
}
