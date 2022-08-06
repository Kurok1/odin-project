package indi.odin.client.kafka;

import indi.odin.client.Channel;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * 回调适配
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class KafkaCallbackAdapter implements Callback {

    private final indi.odin.client.Callback delegate;
    private final Channel channel;
    private final KafkaMessage kafkaMessage;

    public KafkaCallbackAdapter(indi.odin.client.Callback delegate, Channel channel, KafkaMessage kafkaMessage) {
        if (delegate == null)
            throw new NullPointerException();

        this.kafkaMessage = kafkaMessage;
        this.delegate = delegate;
        this.channel = channel;
    }

    @Override
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        if (metadata == null) {
            this.delegate.onFailure(this.kafkaMessage, this.channel, exception.getMessage());
            return;
        }

        this.kafkaMessage.setPartition(metadata.partition());
        this.kafkaMessage.setOffset(metadata.offset());
        if (exception != null) {
            this.delegate.onFailure(this.kafkaMessage, this.channel, exception.getMessage());
        } else this.delegate.onSuccess(this.kafkaMessage, this.channel);
    }

    private boolean isError(RecordMetadata metadata) {
        return metadata.hasOffset() && metadata.offset() == -1;
    }
}
