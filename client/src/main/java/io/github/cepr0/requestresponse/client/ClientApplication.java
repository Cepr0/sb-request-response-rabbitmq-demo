package io.github.cepr0.requestresponse.client;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@EnableConfigurationProperties(DemoProps.class)
@ComponentScan("io.github.cepr0.requestresponse")
@SpringBootApplication
public class ClientApplication {

    public static final String MODEL_EXCHANGE = "model.exchange";

    private final DemoProps props;

    public ClientApplication(DemoProps props) {
        this.props = props;
    }

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(props.getExchange());
    }

    @Bean
    public DirectExchange modelExchange() {
        return new DirectExchange(MODEL_EXCHANGE);
    }

}
