package de.unisiegen.propra.groupfour.braingainmanagement.data.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class CustomerSubject {

    @EmbeddedId
    @Column(nullable = false, updatable = false)
    private CustomerSubjectKey id = new CustomerSubjectKey();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("customerId")
    @JoinColumn(name = "customer_id", columnDefinition = "CHAR(36)")
    @EqualsAndHashCode.Exclude
    private Customer customer;

    @ManyToOne
    @MapsId("subjectId")
    @EqualsAndHashCode.Exclude
    private Subject subject;

    private Integer quota;

    public CustomerSubject(Customer customer, Subject subject, Integer quota) {
        id = new CustomerSubjectKey(customer.getId(), subject.getId());
        this.customer = customer;
        this.subject = subject;
        this.quota = quota;
    }

    @Override
    public String toString(){
        return this.subject.toString()+" ("+this.quota+")";
    }

}
