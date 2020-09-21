package io.github.cepr0.requestresponse.server;

import io.github.cepr0.requestresponse.common.ResponseError;
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
        log.debug("[d] Exception caught: {}", exception.toString());
        Throwable cause = NestedExceptionUtils.getMostSpecificCause(exception);
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
        ResponseError error = new ResponseError(status, errorMessage);
        log.debug("[d] Sending error: {}", error);
        return new ResponseStatusMessage<>(status, error);
    }
}
