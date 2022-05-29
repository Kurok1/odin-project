package indi.odin.consumer;

import indi.odin.Message;
import indi.odin.exception.BindFailedException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 消费监听抽象实现
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public abstract class AbstractListener implements Listener {

    protected final Set<String> registeredQueues = new CopyOnWriteArraySet<>();
    protected final Map<String, MessageProcessor<?>> queueMessageProcessorMap = new ConcurrentHashMap<>();

    private final Object monitorLock = new Object();

    protected volatile boolean isRunning = false;

    public Object getMonitorLock() {
        return this.monitorLock;
    }

    protected void forbidOnRunning() {
        if (isRunning())
            throw new IllegalStateException("current listener is running, cannot do some action");
    }

    @Override
    public void bindQueue(String queueName) {
        forbidOnRunning();
        synchronized (getMonitorLock()) {
            if (registeredQueues.contains(queueName))
                return;

            registeredQueues.add(queueName);
        }
    }

    @Override
    public void unBindQueue(String queueName) {
        forbidOnRunning();
        synchronized (getMonitorLock()) {
            if (!registeredQueues.contains(queueName)) {
                queueMessageProcessorMap.remove(queueName);
                registeredQueues.remove(queueName);
            }
        }
    }

    @Override
    public void unbindMessageProcessor(String queueName) {
        forbidOnRunning();
        synchronized (getMonitorLock()) {
            queueMessageProcessorMap.remove(queueName);
        }
    }

    @Override
    public void start() {
        try {
            startInternal();
            this.isRunning = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    protected abstract void startInternal() throws Exception;

    @Override
    public void stop() {
        try {
            stopInternal();
            this.isRunning = false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract void stopInternal() throws Exception;

    @Override
    public boolean isRunning() {
        return this.isRunning;
    }

    @Override
    public void bindQueues(String... queueNames) {
        forbidOnRunning();
        if (queueNames.length > 0) {
            for (String queueName : queueNames)
                bindQueue(queueName);
        }
    }

    @Override
    public void bindMessageProcessor(String queueName, MessageProcessor<?> messageProcessor) {
        forbidOnRunning();
        synchronized (getMonitorLock()) {
            if (!registeredQueues.contains(queueName))
                return;

            //check bind
            if (queueMessageProcessorMap.containsKey(queueName))
                throw new BindFailedException(messageProcessor.getClass(), queueName, queueMessageProcessorMap.get(queueName).getClass());

            this.queueMessageProcessorMap.put(queueName, messageProcessor);

        }
    }
}
