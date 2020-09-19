package io.github.cepr0.requestresponse.server;

import io.github.cepr0.requestresponse.common.model.Model;
import io.github.cepr0.requestresponse.common.model.ModelMapper;
import io.github.cepr0.requestresponse.common.model.dto.CreateModelRequest;
import io.github.cepr0.requestresponse.common.model.dto.GetAllModelsRequest;
import io.github.cepr0.requestresponse.common.model.dto.ModelResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

import static io.github.cepr0.requestresponse.common.QueueConfig.MODEL_CREATE;
import static io.github.cepr0.requestresponse.common.QueueConfig.MODEL_GET_ALL;

@Slf4j
@Service
public class ModelService {

    private final ModelRepo modelRepo;
    private final ModelMapper modelMapper;

    public ModelService(ModelRepo modelRepo, ModelMapper modelMapper) {
        this.modelRepo = modelRepo;
        this.modelMapper = modelMapper;
    }

    @RabbitListener(queues = MODEL_CREATE)
    public ModelResponse create(@Validated CreateModelRequest request) {
        log.debug("[d] Received: {}", request);
        Model model = modelMapper.toModel(request);
        return modelMapper.toModelResponse(modelRepo.save(model));
    }

    @RabbitListener(queues = MODEL_GET_ALL)
    public List<ModelResponse> getAll(GetAllModelsRequest request) {
        log.debug("[d] Received: {}", request);
        List<ModelResponse> response = new ArrayList<>();
        modelRepo.findAll().forEach(model -> response.add(modelMapper.toModelResponse(model)));
        return response;
    }


}
