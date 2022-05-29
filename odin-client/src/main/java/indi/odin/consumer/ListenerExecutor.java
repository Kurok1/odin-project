package indi.odin.consumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 监听者线程池
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class ListenerExecutor {

    private final ExecutorService executorService;

    public static final ListenerExecutor INSTANCE = new ListenerExecutor();

    private ListenerExecutor() {
        this.executorService = Executors.newCachedThreadPool(new ListenerThreadFactory());
    }

    public void addListener(Listener listener) {
        ListenerThread thread = new ListenerThread(listener);
        executorService.execute(thread);
    }

}
