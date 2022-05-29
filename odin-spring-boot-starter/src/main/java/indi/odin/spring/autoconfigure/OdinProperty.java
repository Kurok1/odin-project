package indi.odin.spring.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 配置项目
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "odin")
public class OdinProperty {

    private List<RabbitMq> rabbitMq;
    private List<Kafka> kafka;
    private List<ConnectionProperty> connectionProperties;

    public List<ConnectionProperty> getConnectionProperties() {
        return connectionProperties;
    }

    public void setConnectionProperties(List<ConnectionProperty> connectionProperties) {
        this.connectionProperties = connectionProperties;
    }

    public List<RabbitMq> getRabbitMq() {
        return rabbitMq;
    }

    public void setRabbitMq(List<RabbitMq> rabbitMq) {
        this.rabbitMq = rabbitMq;
    }

    public List<Kafka> getKafka() {
        return kafka;
    }

    public void setKafka(List<Kafka> kafka) {
        this.kafka = kafka;
    }

    public static class RabbitMq {
        private String connectionSource;
        private String name;

        public String getConnectionSource() {
            return connectionSource;
        }

        public void setConnectionSource(String connectionSource) {
            this.connectionSource = connectionSource;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Kafka {
        private String name;
        private String connectionSource;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getConnectionSource() {
            return connectionSource;
        }

        public void setConnectionSource(String connectionSource) {
            this.connectionSource = connectionSource;
        }
    }

    public static class ConnectionProperty {
        private String name;
        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
