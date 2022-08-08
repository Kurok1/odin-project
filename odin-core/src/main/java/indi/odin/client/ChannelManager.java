package indi.odin.client;

import indi.odin.exception.ChannelDeclaredException;
import indi.odin.exception.ChannelNotFoundException;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管道管理
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class ChannelManager {

    private final ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>(16);

    /**
     * 声明并且注册一个管道
     * @param channel 具体管道
     * @throws ChannelDeclaredException 重复命名
     */
    public void declareChannel(Channel channel) {
        String channelName = channel.name();

        if (channelMap.containsKey(channelName))
            throw new ChannelDeclaredException(channelName);

        channelMap.put(channelName, channel);
    }

    /**
     * 取出管道
     * @param name 管道名称
     * @return 对应管道 non-null
     * @throws ChannelNotFoundException 管道未找到
     */
    public Channel retrieveChannel(String name) {

        Channel channel = channelMap.get(name);
        if (channel == null)
            throw new ChannelNotFoundException(name);

        return channel;

    }

    /**
     * 关闭管道并且取消注册
     * @param name 管道名称
     * @throws IOException 可能抛出的io异常
     * @throws ChannelNotFoundException 管道不存在
     */
    public void closeChannel(String name) throws IOException {
        Channel channel = retrieveChannel(name);

        channel.close();
        channelMap.remove(name);

    }

    public void closeAll() throws IOException {
        for (Map.Entry<String, Channel> entry : channelMap.entrySet()) {
            Channel channel = entry.getValue();
            channel.close();
            channelMap.remove(entry.getKey());
        }

        channelMap.clear();
    }


}
