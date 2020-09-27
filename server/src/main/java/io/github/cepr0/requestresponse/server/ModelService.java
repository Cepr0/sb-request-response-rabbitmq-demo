package io.github.cepr0.requestresponse.server;

import io.github.cepr0.requestresponse.common.ResponseStatusMessage;
import io.github.cepr0.requestresponse.common.model.Model;
import io.github.cepr0.requestresponse.common.model.ModelMapper;
import io.github.cepr0.requestresponse.common.model.dto.CreateModelRequest;
import io.github.cepr0.requestresponse.common.model.dto.GetAllModelsRequest;
import io.github.cepr0.requestresponse.common.model.dto.GetOneModelRequest;
import io.github.cepr0.requestresponse.common.model.dto.ModelResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.github.cepr0.requestresponse.common.QueueConfig.*;

@Slf4j
@Service
@Transactional
@Validated
public class ModelService {

    private final ModelRepo modelRepo;
    private final ModelMapper modelMapper;

    public ModelService(ModelRepo modelRepo, ModelMapper modelMapper) {
        this.modelRepo = modelRepo;
        this.modelMapper = modelMapper;
    }

    @RabbitListener(queues = MODEL_CREATE, errorHandler = "serviceErrorHandler")
    public ResponseStatusMessage<?> create(@Valid CreateModelRequest request) {
        log.debug("[d] Received: {}", request);
        Model model = modelMapper.toModel(request);
        ModelResponse response = modelMapper.toModelResponse(modelRepo.save(model));
        return ResponseStatusMessage.created(response, "/models/" + response.getId());
    }

    @Transactional(readOnly = true)
    @RabbitListener(queues = MODEL_GET_ALL, errorHandler = "serviceErrorHandler")
    public ResponseStatusMessage<?> getAll(GetAllModelsRequest request) {
        log.debug("[d] Received: {}", request);
        List<ModelResponse> response = modelRepo.findAll()
                .stream()
                .map(modelMapper::toModelResponse)
                .collect(Collectors.toList());
        return ResponseStatusMessage.ok(response);
    }

    @Transactional(readOnly = true)
    @RabbitListener(queues = MODEL_GET, errorHandler = "serviceErrorHandler")
    public ResponseStatusMessage<?> getOne(@Valid GetOneModelRequest request) {
        log.debug("[d] Received: {}", request);
        UUID modelId = request.getId();
        ModelResponse response = modelRepo.findById(modelId)
                .map(modelMapper::toModelResponse)
                .orElseThrow(() -> new ResponseStatusException( // TODO Replace to ApiException
                        HttpStatus.NOT_FOUND,
                        "Model not found by id " + modelId
                ));
        // if (true) throw new RuntimeException("test exception"); // for testing
        return ResponseStatusMessage.ok(response);
    }
}
