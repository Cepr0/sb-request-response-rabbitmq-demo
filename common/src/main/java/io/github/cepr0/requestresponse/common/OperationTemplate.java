package io.github.cepr0.requestresponse.common;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.server.ResponseStatusException;

import static java.text.MessageFormat.format;
import static org.springframework.core.log.LogFormatUtils.formatValue;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
public class OperationTemplate {

    private final String baseName;
    private final RabbitTemplate template;

    public OperationTemplate(@NonNull String entityName, @NonNull RabbitTemplate template) {
        this.baseName = entityName;
        this.template = template;
        this.template.setReplyTimeout(2000); // TODO Must be moved to configuration props
    }

    // @Transactional
    public <T, S> S perform(@NonNull String operation, @NonNull T request, ParameterizedTypeReference<S> responseType) {
        log.debug("[d] Performing operation {}.{}: {}", baseName, operation, request);

        String operationName = baseName + "." + operation;
        String errorMessage;
        try {
            S response = template.convertSendAndReceiveAsType(operationName, request, responseType);
            if (response != null) {
                log.debug("[d] Received {} response: {}", operationName, formatValue(response, true));
                return response;
            } else {
                errorMessage = format("Could not perform operation {0}: Received null as a response", operationName);
                log.error("[!] " + errorMessage);
            }
        } catch (AmqpException e) {
            errorMessage = format(
                    "Could not perform operation {0}: {1}",
                    operationName,
                    NestedExceptionUtils.getMostSpecificCause(e).toString()
            );
            log.error("[!] " + errorMessage, e);
        }
        throw new ResponseStatusException(INTERNAL_SERVER_ERROR, errorMessage);
    }
}
