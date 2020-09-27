package io.github.cepr0.requestresponse.common.model.dto;

import lombok.NonNull;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Value
public class GetOneModelRequest {
    @NonNull
    @NotNull
    UUID id;
}
