package io.github.cepr0.requestresponse.common.model.dto;

import lombok.Value;

import javax.validation.constraints.NotEmpty;

@Value
public class CreateModelRequest {
    @NotEmpty String text;
    int num;
}
