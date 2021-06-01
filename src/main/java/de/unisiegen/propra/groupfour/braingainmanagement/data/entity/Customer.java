package de.unisiegen.propra.groupfour.braingainmanagement.data.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Collection;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Customer extends Person {

    @Column(nullable = false)
    private String invoiceStreet, invoiceCity;

    @Column(nullable = false, columnDefinition = "CHAR(5)")
    private String invoiceZipcode;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private Collection<CustomerSubject> subjects;

    public Customer(String prename, String surname, String phone, String email, String street, String city, String zipcode, String invoiceStreet, String invoiceCity, String invoiceZipcode) {
        super(prename, surname, phone, email, street, city, zipcode);
        this.invoiceStreet = invoiceStreet;
        this.invoiceCity = invoiceCity;
        this.invoiceZipcode = invoiceZipcode;
    }

    public Customer(String prename, String surname, String phone, String email, String street, String city, String zipcode) {
        this(prename, surname, phone, email, street, city, zipcode, street, city, zipcode);
    }
}
