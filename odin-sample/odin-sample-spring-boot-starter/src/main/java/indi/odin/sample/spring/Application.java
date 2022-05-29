package indi.odin.sample.spring;

import indi.odin.spring.autoconfigure.EnableOdinClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TODO
 *
 * @author <a href="mailto:hanchao@66yunlian.com">韩超</a>
 * @since 1.0.0
 */
@SpringBootApplication
@EnableOdinClient(basePackages = {"indi.odin.sample.spring"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
