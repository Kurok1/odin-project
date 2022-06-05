package indi.odin.client.kafka;

import indi.odin.MessageMetaData;
import org.apache.kafka.common.header.Header;

import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * kafka元数据
 *
 * @author <a href="mailto:hanchao@66yunlian.com">韩超</a>
 * @since 1.0.0
 */
public class KafkaMetaData extends MessageMetaData {

    private int partition = 0;

    public int getPartition() {
        return this.partition;
    }

    public void setPartition(int partition) {
        this.partition = partition;
    }

    private final Set<Header> headerEntries = new CopyOnWriteArraySet<>();

    public static class HeaderEntry implements Header {
        private String key;
        private String value;

        public HeaderEntry() {
        }

        public HeaderEntry(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String key() {
            return key;
        }

        @Override
        public byte[] value() {
            return value.getBytes(StandardCharsets.UTF_8);
        }
    }

    public void setHeader(String key, String value) {
        this.headerEntries.add(new HeaderEntry(key, value));
    }

    public void addHeader(Header header) {
        this.headerEntries.add(header);
    }

    public Iterable<Header> getHeaderEntries() {
        return headerEntries;
    }
}
