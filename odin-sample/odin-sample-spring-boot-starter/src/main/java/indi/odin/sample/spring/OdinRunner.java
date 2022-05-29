package indi.odin.sample.spring;

import indi.odin.client.Channel;
import indi.odin.spring.autoconfigure.OdinAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
@Component
@DependsOn({OdinAutoConfiguration.INITIALIZE_BEAN})
public class OdinRunner implements ApplicationRunner {

    @Autowired
    @Qualifier("test-odin")
    private Channel channel;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (this.channel != null)
            channel.sendMessage("Hello");
    }
}
