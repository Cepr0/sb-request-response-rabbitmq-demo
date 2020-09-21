package io.github.cepr0.requestresponse.server;

import io.github.cepr0.requestresponse.common.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ModelRepo extends JpaRepository<Model, UUID> {
}
