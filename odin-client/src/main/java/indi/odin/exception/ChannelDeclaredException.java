package indi.odin.exception;

/**
 * 管道重复声明
 *
 * @author <a href="mailto:hanchao@66yunlian.com">韩超</a>
 * @since 1.0.0
 */
public class ChannelDeclaredException extends RuntimeException {

    private static final String template = "the channel [name = %s] has declared";

    public ChannelDeclaredException(String channelName) {
        super(String.format(template, channelName));
    }

}
