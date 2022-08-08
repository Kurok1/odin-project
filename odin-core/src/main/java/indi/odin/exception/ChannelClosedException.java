package indi.odin.exception;

/**
 * 管道关闭异常
 *
 * @author <a href="mailto:hanchao@66yunlian.com">韩超</a>
 * @since 1.0.0
 */
public class ChannelClosedException extends RuntimeException {

    private static final String errorTemplate = "the channel [name = %s] is closed";

    public ChannelClosedException(String channelName) {
        super(String.format(errorTemplate, channelName));
    }
}
