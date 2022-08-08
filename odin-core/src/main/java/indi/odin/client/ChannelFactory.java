package indi.odin.client;

import java.io.IOException;

/**
 * 管道制造工厂
 *
 * @author <a href="mailto:maimengzzz@gamil.com">韩超</a>
 * @since 1.0.0
 * @see Channel
 */
@FunctionalInterface
public interface ChannelFactory<C extends Channel, B extends BasicConfiguration> {

    C create(String name, B configuration) throws IOException;

}
