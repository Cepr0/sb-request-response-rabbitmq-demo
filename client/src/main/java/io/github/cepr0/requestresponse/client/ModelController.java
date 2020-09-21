package io.github.cepr0.requestresponse.client;

import io.github.cepr0.requestresponse.common.OperationTemplate;
import io.github.cepr0.requestresponse.common.model.dto.CreateModelRequest;
import io.github.cepr0.requestresponse.common.model.dto.GetAllModelsRequest;
import io.github.cepr0.requestresponse.common.model.dto.GetOneModelRequest;
import io.github.cepr0.requestresponse.common.model.dto.ModelResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.github.cepr0.requestresponse.common.QueueConfig.*;
import static org.springframework.core.log.LogFormatUtils.formatValue;

@Slf4j
@RestController
@RequestMapping("models")
// @Transactional
public class ModelController {

    private final OperationTemplate modelOperations;

    public ModelController(OperationTemplate modelOperations) {
        this.modelOperations = modelOperations;
    }

    @PostMapping
    public ResponseEntity<ModelResponse> create(@RequestBody CreateModelRequest request) {
        log.debug("[d] Received: {}", request);
        ModelResponse response = modelOperations.perform(
                OPERATION_CREATE,
                request,
                new ParameterizedTypeReference<>() {}
        );
        log.debug("[d] Received: {}", formatValue(response, true));
        return ResponseEntity.created(URI.create("/models/" + response.getId())).body(response);
    }

    @GetMapping
    public List<ModelResponse> getAll() {
        log.debug("[d] Received GetAllModelsRequest");
        List<ModelResponse> response = modelOperations.perform(
                OPERATION_GET_ALL,
                new GetAllModelsRequest(),
                new ParameterizedTypeReference<>() {}
        );
        log.debug("[d] Received List of all ModelResponse: {}", formatValue(response, true));
        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModelResponse> getOne(@PathVariable UUID id) {
        GetOneModelRequest request = new GetOneModelRequest(id);
        log.debug("[d] Received: {}", request);
        Optional<ModelResponse> response = modelOperations.perform(
                OPERATION_GET,
                request,
                new ParameterizedTypeReference<>() {}
        );
        log.debug("[d] Received: {}", formatValue(response, true));
        return ResponseEntity.of(response);
    }
}
