package io.github.cepr0.requestresponse.server;

import io.github.cepr0.requestresponse.common.model.Model;
import io.github.cepr0.requestresponse.common.model.ModelMapper;
import io.github.cepr0.requestresponse.common.model.dto.CreateModelRequest;
import io.github.cepr0.requestresponse.common.model.dto.GetAllModelsRequest;
import io.github.cepr0.requestresponse.common.model.dto.GetOneModelRequest;
import io.github.cepr0.requestresponse.common.model.dto.ModelResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.github.cepr0.requestresponse.common.QueueConfig.*;

@Slf4j
@Service
@Transactional
public class ModelService {

    private final ModelRepo modelRepo;
    private final ModelMapper modelMapper;

    public ModelService(ModelRepo modelRepo, ModelMapper modelMapper) {
        this.modelRepo = modelRepo;
        this.modelMapper = modelMapper;
    }

    @RabbitListener(queues = MODEL_CREATE)
    public ModelResponse create(CreateModelRequest request) {
        log.debug("[d] Received: {}", request);
        Model model = modelMapper.toModel(request);
        return modelMapper.toModelResponse(modelRepo.save(model));
    }

    @Transactional(readOnly = true)
    @RabbitListener(queues = MODEL_GET_ALL)
    public List<ModelResponse> getAll(GetAllModelsRequest request) {
        log.debug("[d] Received: {}", request);
        return modelRepo.findAll()
                .stream()
                .map(modelMapper::toModelResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @RabbitListener(queues = MODEL_GET/*, errorHandler = "customErrorHandler"*/)
    public Optional<ModelResponse> getOne(GetOneModelRequest request) {
        log.debug("[d] Received: {}", request);
        // if (true) throw new RuntimeException("test exception");
        return modelRepo.findById(request.getId()).map(modelMapper::toModelResponse);
    }
}
