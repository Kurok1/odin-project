Quick Start With Rabbitmq

1.add maven dependcies

```xml
<dependencies>
		<dependency>
    		<groupId>indi.odin</groupId>
        <artifactId>odin-client</artifactId>
        <version>${odin.version}</version>
    </dependency>
</dependencies>
```

2.make producer application and run

```java
public class Producer {

    private static final String connectionStr = "rabbitmq://localhost:5672/test-odin?vhost=/test-odin&username=guest&password=guest";

    public static void main(String[] args) throws Exception {
        ConnectionSource<RabbitmqConfiguration> connectionSource = new RabbitConnectionSource("test-producer", connectionStr);

        RabbitmqConfiguration configuration = (RabbitmqConfiguration) connectionSource.resolveConfiguration();
        Serializers serializers = new Serializers();
        RabbitmqMessageAssembler messageAssembler = new RabbitmqMessageAssembler(serializers);
        RabbitmqChannelFactory channelFactory = new RabbitmqChannelFactory(messageAssembler);

        Channel channel = channelFactory.create("test-producer", configuration);

        channel.sendMessage("Hello Odin", new Callback() {
            @Override
            public void onSuccess(Message message, Channel channel) {
                System.out.println("send message succeed");
            }

            @Override
            public void onFailure(Message message, Channel channel, String errorMessage) {
                System.out.printf("send message failed with error %s \n", errorMessage);
            }
        });

    }

}
```

3.make consumer application and run

```java
public class Consumer {

    private static final String connectionStr = "rabbitmq://localhost:5672/?vhost=/test-odin&username=guest&password=guest";
    private static final String bindQueue = "test-odin";

    public static void main(String[] args) throws Exception {
        final ConnectionSource<RabbitmqConfiguration> connectionSource = new RabbitConnectionSource("test-consumer", connectionStr);
        RabbitmqConfiguration configuration = (RabbitmqConfiguration) connectionSource.resolveConfiguration();

        Listener listener = ListenerFactory.createListener(connectionSource.getProduct(), configuration);

        listener.bindQueues(bindQueue);
        listener.bindMessageProcessor(bindQueue, new MessageProcessorImpl());

        ListenerExecutor.INSTANCE.addListener(listener);
    }

    public static class MessageProcessorImpl implements MessageProcessor<RabbitmqMessage> {
        private final String template = "received message from queue [%s] and deliveryTag is [%d] and message body is %s";

        @Override
        public HandleResponse onMessage(String queueName, RabbitmqMessage message) {
            String body = new String(message.getSources(), StandardCharsets.UTF_8);
            long deliveryTag = message.metaData().getDeliveryTag();
            System.out.println(String.format(template, queueName, deliveryTag, body));
            return HandleResponse.SUCCESS;
        }
    }

}
```



Spring Boot Starter

1.add maven dependcies

```xml
<dependencies>
        <dependency>
            <groupId>indi.odin</groupId>
            <artifactId>odin-spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>indi.odin</groupId>
            <artifactId>odin-spring-boot-starter-rabbit</artifactId>
        </dependency>
</dependencies>
```

2.prepare application.yml

```yaml
odin:
    connectionProperties:
        - name: "test"
          url: "rabbitmq://localhost:5672/test-odin?vhost=/test-odin&username=guest&password=guest"
        - name: "test-consumer"
          url: "rabbitmq://localhost:5672/?vhost=/test-odin&username=guest&password=guest"
    rabbitmq:
        - name: "test-odin"
          connectionSource: "test"
```

3.autowired `Channel`

```java
@Component
@DependsOn({OdinAutoConfiguration.INITIALIZE_RABBIT_BEAN})
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
```

4.enable odin client and configure your processor packages

```java
@EnableOdinClient(basePackages={"your.package"})
```



5.register message processor

```
@MessageListener(bindingQueue = {"test-odin"}, connectionKey = "test-consumer")
public class TestConsumer implements MessageProcessor<RabbitmqMessage> {

    @Override
    public HandleResponse onMessage(String queueName, RabbitmqMessage message) {
        String str = new String(message.getSources(), StandardCharsets.UTF_8);
        System.out.printf("received queue [%s] and body is [%s]", queueName, str);
        return HandleResponse.SUCCESS;
    }


}
```

5.see more with [spring-boot-starter-sample](./odin-sample/odin-sample-spring-boot-starter)

