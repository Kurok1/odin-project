package indi.odin.consumer;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 监听者线程工厂
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class ListenerThreadFactory implements ThreadFactory {

    private final String prefix = "odin-listener";
    private final AtomicInteger index = new AtomicInteger(0);


    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, String.format("%s-%d", prefix, index.getAndIncrement()));
    }
}
