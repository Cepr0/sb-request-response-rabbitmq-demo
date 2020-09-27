package io.github.cepr0.requestresponse.common;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

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
        // this.template.setChannelTransacted(true); // TODO Think about decoupling read-only and write transactions
    }

    // @Transactional
    public <T> ResponseEntity<?> perform(@NonNull String operation, @NonNull T request) {
        log.debug("[d] Performing operation {}.{}: {}", baseName, operation, request);

        String operationName = baseName + "." + operation;
        String errorMessage;
        try {
            ResponseStatusMessage<?> response = template.convertSendAndReceiveAsType(
                    operationName,
                    request,
                    new ParameterizedTypeReference<>() {}
            );
            if (response != null) {
                log.debug("[d] Received {} response: {}", operationName, formatValue(response, true));
                HttpStatus status = response.getStatus();
                if (status != null) {
                    if (status.is2xxSuccessful()) {
                        var headers = response.httpHeaders();
                        return ResponseEntity.status(status).headers(headers).body(response.getPayload());
                    } else {
                        var payload = ((Map<?, ?>) response.getPayload());
                        throw new ResponseStatusException(status, ((String) payload.get("message")));
                    }
                } else {
                    errorMessage = "Received response with status = null";
                    log.error("[!] {}", errorMessage);
                }
            } else {
                errorMessage = format("Could not perform operation {0}: Received null as a response", operationName);
                log.error("[!] {}", errorMessage);
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
