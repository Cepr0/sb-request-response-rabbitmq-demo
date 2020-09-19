package io.github.cepr0.requestresponse.common.model;

import io.github.cepr0.requestresponse.common.model.dto.CreateModelRequest;
import io.github.cepr0.requestresponse.common.model.dto.ModelResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
        nullValueMappingStrategy = RETURN_DEFAULT,
        nullValueCheckStrategy = ALWAYS,
        nullValuePropertyMappingStrategy = IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ModelMapper {

    @Mapping(target = "id", expression = "java(UUID.randomUUID())")
    Model toModel(CreateModelRequest request);

    ModelResponse toModelResponse(Model model);
}
