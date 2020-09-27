package io.github.cepr0.requestresponse.server;

import io.github.cepr0.requestresponse.common.ErrorResponse;
import io.github.cepr0.requestresponse.common.ResponseStatusMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Component("serviceErrorHandler")
public class ServiceErrorHandler implements RabbitListenerErrorHandler {
    @Override
    public Object handleError(
            Message amqpMessage,
            org.springframework.messaging.Message<?> message,
            ListenerExecutionFailedException exception
    ) {
        Throwable cause = NestedExceptionUtils.getMostSpecificCause(exception);
        log.debug("[d] Caught an exception in {}", exception.getMessage());
        log.debug("[d] Exception is {}", cause.toString());
        HttpStatus status;
        String errorMessage;
        if (cause instanceof ResponseStatusException) { // TODO Replace to ApiException
            var e = (ResponseStatusException) cause;
            status = e.getStatus();
            errorMessage = e.getReason();
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            errorMessage = cause.getMessage();
        }
        ErrorResponse error = new ErrorResponse(status, errorMessage);
        log.debug("[d] Sending error: {}", error);
        // if (true) throw new RuntimeException("test exception");
        return new ResponseStatusMessage<>(status, error);
    }
}
