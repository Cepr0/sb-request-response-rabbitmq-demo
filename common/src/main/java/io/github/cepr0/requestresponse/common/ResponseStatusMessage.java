package io.github.cepr0.requestresponse.common;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.NonNull;
import lombok.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Value
public class ResponseStatusMessage<T> implements Serializable {
    @NonNull HttpStatus status;
    @NonNull T payload;
    @JsonAnySetter Map<String, String> headers = new HashMap<>();

    public static <T> ResponseStatusMessage<T> ok(@NonNull T payload) {
        return new ResponseStatusMessage<>(HttpStatus.OK, payload);
    }

    public static <T> ResponseStatusMessage<T> notFound(@NonNull T payload) {
        return new ResponseStatusMessage<>(HttpStatus.NOT_FOUND, payload);
    }

    public static <T> ResponseStatusMessage<T> internalServerError(@NonNull T payload) {
        return new ResponseStatusMessage<>(HttpStatus.INTERNAL_SERVER_ERROR, payload);
    }

    public static <T> ResponseStatusMessage<T>  created(@NonNull T payload, @NonNull String location) {
        return new ResponseStatusMessage<>(HttpStatus.CREATED, payload).addLocation(location);
    }

    public ResponseStatusMessage<T> addLocation(@NonNull String location) {
        headers.put(HttpHeaders.LOCATION, location);
        return this;
    }

    public ResponseStatusMessage<T> addHeader(@NonNull String header, @NonNull String value) {
        headers.put(header, value);
        return this;
    }

    public Optional<String> getHeader(@NonNull String header) {
        return Optional.ofNullable(headers.get(header));
    }

    public HttpHeaders httpHeaders() {
        return new HttpHeaders(new LinkedMultiValueMap<>(headers.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> List.of(e.getValue()),
                        (list1, list2) -> list2,
                        HashMap::new
                ))));
    }
}
