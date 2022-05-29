package indi.odin.client.rabbitmq;

import com.rabbitmq.client.Return;
import com.rabbitmq.client.ReturnCallback;
import indi.odin.client.Callback;
import indi.odin.client.Channel;

/**
 * 推送反馈适配
 *
 * @author <a href="mailto:hanchao@66yunlian.com">韩超</a>
 * @since 1.0.0
 */
public class ReturnCallbackAdapter implements ReturnCallback {

    private final Channel channel;
    private final Callback callback;

    public ReturnCallbackAdapter(Channel channel, Callback callback) {
        this.channel = channel;
        this.callback = callback;
    }

    protected RabbitmqMessage recover(Return returnMessage) {
        RabbitmqMessageMetaData metaData = new RabbitmqMessageMetaData();
        metaData.setExchange(returnMessage.getExchange());
        metaData.setRoutingKey(returnMessage.getRoutingKey());
        metaData.setBasicProperties(returnMessage.getProperties());

        return new RabbitmqMessage(returnMessage.getBody(), metaData);
    }

    @Override
    public void handle(Return returnMessage) {
        RabbitmqMessage message = recover(returnMessage);

        //只有失败才会触发
        this.callback.onFailure(message, this.channel, returnMessage.getReplyText());
    }

}
