package de.unisiegen.propra.groupfour.braingainmanagement.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSubjectKey implements Serializable {

    @Type(type = "uuid-char")
    private UUID customerId;

    private String subjectId;

}
