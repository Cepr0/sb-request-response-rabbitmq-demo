package io.github.cepr0.requestresponse.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {

    public static final String MODEL = "model";
    public static final String OPERATION_CREATE = "create";
    public static final String OPERATION_UPDATE = "update";
    public static final String OPERATION_DELETE = "delete";
    public static final String OPERATION_GET = "get";
    public static final String OPERATION_GET_ALL = "get-all";
    public static final String MODEL_CREATE = MODEL + "." + OPERATION_CREATE;
    public static final String MODEL_UPDATE = MODEL + "." + OPERATION_UPDATE;
    public static final String MODEL_DELETE = MODEL + "." + OPERATION_DELETE;
    public static final String MODEL_GET = MODEL + "." + OPERATION_GET;
    public static final String MODEL_GET_ALL = MODEL + "." + OPERATION_GET_ALL;

    @Bean
    public MessageConverter messageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public Declarables bindings() {
        Queue modelCreateQ = QueueBuilder.nonDurable(MODEL_CREATE).build();
        Queue modelUpdateQ = QueueBuilder.nonDurable(MODEL_UPDATE).build();
        Queue modelDeleteQ = QueueBuilder.nonDurable(MODEL_DELETE).build();
        Queue modelGetQ = QueueBuilder.nonDurable(MODEL_GET).build();
        Queue modelGetAllQ = QueueBuilder.nonDurable(MODEL_GET_ALL).build();

        DirectExchange modelCreateEx = ExchangeBuilder.directExchange(MODEL_CREATE).build();
        DirectExchange modelUpdateEx = ExchangeBuilder.directExchange(MODEL_UPDATE).build();
        DirectExchange modelDeleteEx = ExchangeBuilder.directExchange(MODEL_DELETE).build();
        DirectExchange modelGetEx = ExchangeBuilder.directExchange(MODEL_GET).build();
        DirectExchange modelGetAllEx = ExchangeBuilder.directExchange(MODEL_GET_ALL).build();

        return new Declarables(
                modelCreateQ,
                modelUpdateQ,
                modelDeleteQ,
                modelGetQ,
                modelGetAllQ,
                modelCreateEx,
                modelUpdateEx,
                modelDeleteEx,
                modelGetEx,
                modelGetAllEx,
                BindingBuilder.bind(modelCreateQ).to(modelCreateEx).withQueueName(),
                BindingBuilder.bind(modelUpdateQ).to(modelUpdateEx).withQueueName(),
                BindingBuilder.bind(modelDeleteQ).to(modelDeleteEx).withQueueName(),
                BindingBuilder.bind(modelGetQ).to(modelGetEx).withQueueName(),
                BindingBuilder.bind(modelGetAllQ).to(modelGetAllEx).withQueueName()
        );
    }
}
