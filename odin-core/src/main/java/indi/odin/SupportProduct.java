package indi.odin;

import indi.odin.exception.NoSupportedMQException;

/**
 * 目前支持的产品
 *
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public enum SupportProduct {
    RABBITMQ("rabbitmq"),
    KAFKA("kafka"),
    ROCKETMQ("rocketmq");

    private final String protocol;

    SupportProduct(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }

    public static SupportProduct read(String protocol) {
        switch (protocol) {
            case "rabbitmq":return RABBITMQ;
            case "kafka":return KAFKA;
            case "rocketmq":return ROCKETMQ;
            default:throw new NoSupportedMQException(protocol);
        }
    }
}
