package io.github.cepr0.requestresponse.common.model.dto;

import lombok.Value;

import java.util.UUID;

@Value
public class ModelResponse {
    UUID id;
    String text;
    int num;
}
