package indi.odin.exception;

/**
 * 不支持的消息队列产品
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class NoSupportedMQException extends RuntimeException {

    public NoSupportedMQException(String mqProtocol) {
        super(mqProtocol);
    }
}
