package io.github.cepr0.requestresponse.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("demo")
public class DemoProps {
    private String exchange = "demo.exchange";
    private String routingKey = "demo.key";
}
