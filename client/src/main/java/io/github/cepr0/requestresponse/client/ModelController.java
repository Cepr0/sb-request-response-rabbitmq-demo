package io.github.cepr0.requestresponse.client;

import io.github.cepr0.requestresponse.common.OperationTemplate;
import io.github.cepr0.requestresponse.common.model.dto.CreateModelRequest;
import io.github.cepr0.requestresponse.common.model.dto.GetAllModelsRequest;
import io.github.cepr0.requestresponse.common.model.dto.GetOneModelRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> create(@RequestBody CreateModelRequest request) {
        log.debug("[d] Received: {}", request);
        ResponseEntity<?> response = modelOperations.perform(OPERATION_CREATE, request);
        log.debug("[d] Received: {}", formatValue(response.getBody(), true));
        return response;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        log.debug("[d] Received GetAllModelsRequest");
        ResponseEntity<?> response = modelOperations.perform(OPERATION_GET_ALL, new GetAllModelsRequest());
        log.debug("[d] Received: {}", formatValue(response.getBody(), true));
        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable UUID id) {
        GetOneModelRequest request = new GetOneModelRequest(id);
        log.debug("[d] Received: {}", request);
        ResponseEntity<?> response = modelOperations.perform(OPERATION_GET, request);
        log.debug("[d] Received: {}", formatValue(response.getBody(), true));
        return response;
    }
}
