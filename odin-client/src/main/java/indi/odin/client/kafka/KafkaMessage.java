package indi.odin.client.kafka;

import indi.odin.Message;
import indi.odin.MessageMetaData;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.Serializable;

/**
 * kafka消息封装
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class KafkaMessage<T extends Serializable> implements Message<T> {

    private final KafkaMetaData metaData;

    private final Object source;

    private int partition = 0;
    private long offset = 0L;

    private String key;

    public KafkaMessage(KafkaMetaData metaData, Object source) {
        this.metaData = metaData;
        this.source = source;
        this.partition = metaData.getPartition();
    }

    @Override
    public byte[] getSources() {
        return new byte[0];
    }

    @Override
    public T getSource() {
        return (T)this.source;
    }

    @Override
    public MessageMetaData metaData() {
        return this.metaData;
    }

    public int getPartition() {
        return partition;
    }

    public void setPartition(int partition) {
        this.partition = partition;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ProducerRecord<String, Object> convert() {
        String topic = metaData.getQueueName();
        int partition = this.metaData.getPartition();
        return new ProducerRecord<>(topic, partition, null, null, this.source, this.metaData.getHeaderEntries());
    }
}
