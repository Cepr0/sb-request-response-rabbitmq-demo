package io.github.cepr0.requestresponse.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("demo")
public class DemoController {

    private final DemoProps props;
    private final RabbitTemplate template;

    public DemoController(DemoProps props, RabbitTemplate template) {
        this.props = props;
        this.template = template;
    }

    @PostMapping
    public Object demo(@RequestBody Map<String, Object> message) {
        log.debug("[d] Message to send: {}", message);
        Map<String, Object> response = template.convertSendAndReceiveAsType(
                props.getExchange(),
                props.getRoutingKey(),
                message,
                new ParameterizedTypeReference<>() {}
        );
        log.debug("[d] Received response: {}", response);
        return response;
    }
}
