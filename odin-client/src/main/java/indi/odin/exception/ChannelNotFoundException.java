package indi.odin.exception;

/**
 * 管道未找到
 *
 * @author <a href="mailto:hanchao@66yunlian.com">韩超</a>
 * @since 1.0.0
 */
public class ChannelNotFoundException extends RuntimeException {

    private static final String errorTemplate = "the channel [name = %s] not found, did you forget declare?";

    public ChannelNotFoundException(String channelName) {
        super(String.format(errorTemplate, channelName));
    }
}
