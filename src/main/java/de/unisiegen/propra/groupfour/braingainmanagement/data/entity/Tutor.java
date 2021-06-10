package de.unisiegen.propra.groupfour.braingainmanagement.data.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Tutor extends Person {

    @Column(nullable = false)
    private String bic, iban;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private Collection<Subject> subjects;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private Collection<Customer> customers;

    public Tutor(String prename, String surname, String phone, String email, String street, String city, String zipcode, String bic, String iban) {
        super(prename, surname, phone, email, street, city, zipcode);
        this.bic = bic;
        this.iban = iban;
    }

    @Override
    public String toString(){
        return getFullName();

    }
}
