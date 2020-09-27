package io.github.cepr0.requestresponse.common;

import lombok.Value;
import lombok.experimental.Tolerate;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Value
public class ErrorResponse {
    Instant timestamp;
    int status;
    String error;
    String message;

    // TODO Add static factory methods like in ResponseStatusMessage
    @Tolerate
    public ErrorResponse(HttpStatus httpStatus, String message) {
        this.timestamp = Instant.now();
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.message = message;
    }
}
