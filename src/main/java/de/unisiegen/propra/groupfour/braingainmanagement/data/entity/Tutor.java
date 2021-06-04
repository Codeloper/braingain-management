package de.unisiegen.propra.groupfour.braingainmanagement.data.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Collection;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Tutor extends Person {

    @Column(nullable = false)
    private String bic, iban;

    @OneToMany
    private Collection<Subject> subjects;

    public Tutor(String prename, String surname, String phone, String email, String street, String city, String zipcode, String bic, String iban) {
        super(prename, surname, phone, email, street, city, zipcode);
        this.bic = bic;
        this.iban = iban;
    }
}
