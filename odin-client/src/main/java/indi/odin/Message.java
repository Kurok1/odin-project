package indi.odin;

import java.io.Serializable;

/**
 * 消息定义
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public interface Message<T> {

    byte[] getSources();

    MessageMetaData metaData();

    T getSource();

}
