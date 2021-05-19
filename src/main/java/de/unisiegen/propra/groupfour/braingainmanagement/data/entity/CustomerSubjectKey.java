package de.unisiegen.propra.groupfour.braingainmanagement.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSubjectKey implements Serializable {

    private UUID customerId;

    private String subjectId;

}
