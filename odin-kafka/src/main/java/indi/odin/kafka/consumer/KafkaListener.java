package indi.odin.kafka.consumer;


import indi.odin.client.BasicConfiguration;
import indi.odin.consumer.AbstractListener;
import indi.odin.consumer.HandleResponse;
import indi.odin.consumer.MessageProcessor;
import indi.odin.kafka.client.KafkaConfiguration;
import indi.odin.kafka.client.KafkaMessage;
import indi.odin.kafka.client.KafkaMetaData;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * kafka监听
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.3.0
 */
public class KafkaListener extends AbstractListener {

    private final KafkaConsumer<String, Object> kafkaConsumer;

    private final Duration duration = Duration.ofMillis(1000);


    public KafkaListener(BasicConfiguration configuration) {
        KafkaConfiguration kafkaConfiguration = (KafkaConfiguration) configuration;
        Properties properties = new Properties();
        properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, kafkaConfiguration.getBootstrapServerList());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaDeserializerAdapter.class.getName());
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaDeserializerAdapter.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConfiguration.getGroupId());
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        this.kafkaConsumer = new KafkaConsumer<>(properties);
    }


    @Override
    protected void startInternal() throws Exception {
        this.kafkaConsumer.subscribe(registeredQueues);
        while (isRunning()) {
            ConsumerRecords<String, Object> consumerRecords = this.kafkaConsumer.poll(duration);
            if (!consumerRecords.isEmpty()) {
                boolean needSeek = false;
                Map<TopicPartition, OffsetAndMetadata> commitData = new HashMap<>();
                TopicPartition topicPartition = null;
                OffsetAndMetadata offsetAndMetadata = null;
                for (ConsumerRecord<String, Object> consumerRecord : consumerRecords) {
                    String topic = consumerRecord.topic();
                    int partition = consumerRecord.partition();
                    HandleResponse response = dispatch(consumerRecord.topic(), consumerRecord);
                    topicPartition = new TopicPartition(topic, partition);
                    offsetAndMetadata = new OffsetAndMetadata(consumerRecord.offset() + 1);
                    if (HandleResponse.SUCCESS.equals(response) || HandleResponse.FAILURE.equals(response)) {
                        commitData.put(topicPartition, offsetAndMetadata);
                    } else if (HandleResponse.NEED_RESEND.equals(response)) {
                        offsetAndMetadata = new OffsetAndMetadata(consumerRecord.offset());
                        needSeek = true;
                        break;//提前退出，等待下次重试
                    }
                }
                this.kafkaConsumer.commitSync(commitData);
                if (needSeek)
                    this.kafkaConsumer.seek(topicPartition, offsetAndMetadata);
            }


        }
    }

    protected HandleResponse dispatch(String topic, ConsumerRecord<String, Object> consumerRecord) {
        //convert to kafka message
        KafkaMetaData metaData = new KafkaMetaData();
        consumerRecord.headers().forEach(metaData::addHeader);
        metaData.setPartition(consumerRecord.partition());
        metaData.setQueueName(topic);
        KafkaMessage<?> kafkaMessage = new KafkaMessage<>(metaData, consumerRecord.value());
        kafkaMessage.setOffset(consumerRecord.offset());
        kafkaMessage.setKey(consumerRecord.key());

        MessageProcessor<KafkaMessage<?>> messageProcessor = (MessageProcessor<KafkaMessage<?>>) queueMessageProcessorMap.get(topic);
        if (messageProcessor != null) {
            HandleResponse handleResponse = messageProcessor.onMessage(topic, kafkaMessage);
            return handleResponse;
        }

        return HandleResponse.SUCCESS;
    }

    @Override
    protected void stopInternal() throws Exception {
        this.kafkaConsumer.unsubscribe();
        this.kafkaConsumer.close();
    }
}
