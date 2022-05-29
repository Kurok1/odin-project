package indi.odin;

/**
 * 消息定义
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public interface Message {

    byte[] getSources();

    MessageMetaData metaData();

}
