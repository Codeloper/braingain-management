package de.unisiegen.propra.groupfour.braingainmanagement.data.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Customer extends Person {

    @Column(nullable = false)
    private String invoiceStreet, invoiceCity;

    @Column(nullable = false)
    private String invoiceZipcode;

}
