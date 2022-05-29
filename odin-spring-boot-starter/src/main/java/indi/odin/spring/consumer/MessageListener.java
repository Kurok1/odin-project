package indi.odin.spring.consumer;

import java.lang.annotation.*;

/**
 * 消息监听
 *
 * @author <a href="mailto:hanchao@66yunlian.com">韩超</a>
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessageListener {

    String[] bindingQueue();

    String connectionKey();

}
