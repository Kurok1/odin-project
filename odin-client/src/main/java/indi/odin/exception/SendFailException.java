package indi.odin.exception;

/**
 * 发送异常
 *
 * @author <a href="mailto:maimengzzz@gamil.com">韩超</a>
 * @since 1.0.0
 */
public class SendFailException extends RuntimeException {

    public SendFailException() {
    }

    public SendFailException(String message) {
        super(message);
    }

    public SendFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public SendFailException(Throwable cause) {
        super(cause);
    }

    public SendFailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
