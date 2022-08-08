package indi.odin.consumer;


import indi.odin.Message;

/**
 * 消费监听
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public interface Listener {

    /**
     * 绑定队列
     * @param queueName 队列名称
     */
    void bindQueue(String queueName);

    /**
     * 绑定队列
     * @param queueNames 队列集合
     */
    void bindQueues(String... queueNames);

    /**
     * 取消绑定队列，包括已经绑定的所有消息处理
     * @param queueName
     */
    void unBindQueue(String queueName);

    /**
     * 绑定消息消费者
     * @param queueName 队列
     * @param messageProcessor 对应的消费者
     * @param bindMode 绑定模式，共享或独占，互斥操作
     * @throws indi.odin.exception.BindFailedException 绑定失败
     */
    void bindMessageProcessor(String queueName, MessageProcessor<?> messageProcessor);

    /**
     * 取消绑定
     * @param queueName 队列名称
     * @param processorId 消费者唯一id
     */
    void unbindMessageProcessor(String queueName);

    /**
     * 开启监听
     */
    void start();

    /**
     * 关闭监听
     */
    void stop();

    /**
     * @return 是否运行
     */
    boolean isRunning();

}
