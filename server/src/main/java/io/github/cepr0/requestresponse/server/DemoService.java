package io.github.cepr0.requestresponse.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class DemoService {
    @RabbitListener(queues = "${demo.queue}", concurrency = "1")
    public Map<String, Object> receive(Map<String, Object> message) {
        log.debug("[d] Received message: {}", message);
        message.put("response", "hi");
        return message;
    }
}
