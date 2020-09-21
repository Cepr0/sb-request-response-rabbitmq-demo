package io.github.cepr0.requestresponse.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@Entity
public class Model {
    @Id UUID id;
    @Version Short version;
    String text;
    int num;
}
