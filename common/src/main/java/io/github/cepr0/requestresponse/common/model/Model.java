package io.github.cepr0.requestresponse.common.model;

import lombok.Value;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Value
public class Model {
    @Id UUID id;
    String text;
    int num;
}
