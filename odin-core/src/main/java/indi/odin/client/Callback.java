package indi.odin.client;

import indi.odin.Message;

/**
 * 发送回调
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public interface Callback {

    void onSuccess(Message message, Channel channel);

    void onFailure(Message message, Channel channel, String errorMessage);


    Callback NOTHING = new Callback() {
        @Override
        public void onSuccess(Message message, Channel channel) {

        }

        @Override
        public void onFailure(Message message, Channel channel, String errorMessage) {

        }
    };

}
