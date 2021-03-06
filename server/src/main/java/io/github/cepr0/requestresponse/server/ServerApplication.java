package io.github.cepr0.requestresponse.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.listener.FatalExceptionStrategy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@EntityScan("io.github.cepr0.requestresponse.common.model")
@EnableConfigurationProperties(DemoProps.class)
@ComponentScan("io.github.cepr0.requestresponse")
@SpringBootApplication
public class ServerApplication {

    private final DemoProps props;

    public ServerApplication(DemoProps props) {
        this.props = props;
    }

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(props.getExchange());
    }

    @Bean
    public Queue queue() {
        return new Queue(props.getQueue());
    }

    @Bean
    public Binding binding(DirectExchange directExchange, Queue queue) {
        return BindingBuilder.bind(queue).to(directExchange).with(props.getRoutingKey());
    }

    @Bean
    public FatalExceptionStrategy customExceptionStrategy() { // TODO: Remove it - probably it doesn't work
        return new FatalExceptionStrategy() {
            @Override
            public boolean isFatal(Throwable t) {
                log.debug("[d] FatalExceptionStrategy caught exception: {}", t.toString());
                return true;
            }
        };
    }
}
