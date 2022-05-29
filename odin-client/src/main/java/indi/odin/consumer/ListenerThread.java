package indi.odin.consumer;

import java.util.Objects;

/**
 * 监听者线程
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class ListenerThread implements Runnable {

    private final Listener listener;

    public ListenerThread(Listener listener) {
        Objects.requireNonNull(listener);
        this.listener = listener;
    }

    @Override
    public void run() {
        listener.start();
    }
}
