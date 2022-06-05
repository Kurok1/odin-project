package indi.odin.client.kafka;

import indi.odin.client.MessageAssembler;
import indi.odin.MessageMetaData;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.io.Serializable;

/**
 * Kafka消息组装
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 * @see ProducerRecord
 */
public class KafkaMessageAssembler implements MessageAssembler<KafkaMessage> {

    @Override
    public <E extends Serializable> KafkaMessage<E> mapping(E data, MessageMetaData metaData) throws IOException {
        return new KafkaMessage<>((KafkaMetaData) metaData, data);
    }
}
