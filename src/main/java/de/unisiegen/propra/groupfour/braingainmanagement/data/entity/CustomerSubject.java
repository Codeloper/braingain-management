package de.unisiegen.propra.groupfour.braingainmanagement.data.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class CustomerSubject {

    @EmbeddedId
    @Column(nullable = false, updatable = false)
    private CustomerSubjectKey id = new CustomerSubjectKey();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("customerId")
    @JoinColumn(name = "customer_id", columnDefinition = "CHAR(36)")
    private Customer customer;

    @ManyToOne
    @MapsId("subjectId")
    private Subject subject;

    private Integer quota;

}
