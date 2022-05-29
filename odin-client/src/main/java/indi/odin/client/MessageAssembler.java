package indi.odin.client;

import indi.odin.MessageMetaData;

import java.io.IOException;

/**
 * 消息加工，适配各个消息队列
 *
 * @author <a href="mailto:hanchao@66yunlian.com">韩超</a>
 * @since 1.0.0
 * @param <T> 具体消息队列服务所支持的消息
 */
@FunctionalInterface
public interface MessageAssembler<T> {

    /**
     * 消息映射
     * @param data 实际发送数据
     * @param metaData 元数据
     * @return 转换后消息队列所能识别的
     */
    T mapping(Object data, MessageMetaData metaData) throws IOException;

}
