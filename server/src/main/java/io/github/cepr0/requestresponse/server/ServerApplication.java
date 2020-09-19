package io.github.cepr0.requestresponse.server;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.map.repository.config.EnableMapRepositories;

@EnableMapRepositories
@EnableConfigurationProperties(DemoProps.class)
@ComponentScan("io.github.cepr0.requestresponse")
@SpringBootApplication
public class ServerApplication {

    public static final String MODEL_EXCHANGE = "model.exchange";
    public static final String MODEL_QUEUE = "model.queue";
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

    // @Bean
    // public DirectExchange modelExchange() {
    //     return new DirectExchange(MODEL_EXCHANGE);
    // }

    @Bean
    public Queue queue() {
        return new Queue(props.getQueue());
    }

    // @Bean
    // public Queue modelQueue() {
    //     return new Queue(MODEL_QUEUE);
    // }

    @Bean
    public Binding binding(DirectExchange directExchange, Queue queue) {
        return BindingBuilder.bind(queue).to(directExchange).with(props.getRoutingKey());
    }
}
