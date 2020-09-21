package io.github.cepr0.requestresponse.common;

import lombok.Value;
import lombok.With;
import lombok.experimental.Tolerate;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Value
public class ResponseError {
    Instant timestamp;
    int status;
    String error;
    String message;
    @With String path;

    @Tolerate
    public ResponseError(HttpStatus httpStatus, String message) {
        this.timestamp = Instant.now();
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.message = message;
        this.path = null;
    }
}
