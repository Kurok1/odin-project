package indi.odin.spring.autoconfigure;

import indi.odin.client.ChannelManager;
import org.springframework.beans.factory.DisposableBean;

/**
 * 适配管理
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class ChannelManagerBean extends ChannelManager implements DisposableBean {

    @Override
    public void destroy() throws Exception {
        super.closeAll();
    }
}
