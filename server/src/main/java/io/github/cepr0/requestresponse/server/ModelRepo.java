package io.github.cepr0.requestresponse.server;

import io.github.cepr0.requestresponse.common.model.Model;
import org.springframework.data.keyvalue.repository.KeyValueRepository;

import java.util.UUID;

public interface ModelRepo extends KeyValueRepository<Model, UUID> {
}
