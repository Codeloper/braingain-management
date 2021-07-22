package de.unisiegen.propra.groupfour.braingainmanagement.data.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Tutor extends Person {

    @Column(nullable = false)
    private String bic, iban;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Subject> subjects;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Customer> customers;


    /**
     * Adds a subject the teacher can teach
     * @param subject subject to add
     */
    public void addSubject(Subject subject) {
        if(subjects == null)
            subjects = new HashSet<>();

        subjects.add(subject);
    }

    @Override
    public String toString(){
        return getFullName();
    }

    public Tutor(String prename, String surname, String phone, String email, String street, String city, String zipcode, String bic, String iban) {
        super(prename, surname, phone, email, street, city, zipcode);
        this.bic = bic;
        this.iban = iban;
    }

}
