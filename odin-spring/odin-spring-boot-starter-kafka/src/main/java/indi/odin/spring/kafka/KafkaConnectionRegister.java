package indi.odin.spring.kafka;

import indi.odin.SupportProduct;
import indi.odin.kafka.KafkaConnectionSource;
import indi.odin.kafka.client.KafkaConfiguration;
import indi.odin.spring.autoconfigure.ConnectionRegister;

import java.net.URL;

/**
 * kafka connection register
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.3.0
 */
public class KafkaConnectionRegister extends ConnectionRegister<KafkaConfiguration, KafkaConnectionSource> {

    private final String KAFKA_PROTOCOL = SupportProduct.KAFKA.getProtocol();

    @Override
    protected String getSupportProductProtocol() {
        return KAFKA_PROTOCOL;
    }

    @Override
    protected KafkaConnectionSource toConnectionSource(String name, URL url) {
        return new KafkaConnectionSource(name, url);
    }
}
