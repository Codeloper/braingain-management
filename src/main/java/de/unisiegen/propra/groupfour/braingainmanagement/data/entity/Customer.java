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
public class Customer extends Person {

    @Column(nullable = false)
    private String invoiceStreet, invoiceCity;

    @Column(nullable = false/*, columnDefinition = "CHAR(5)"*/)
    private String invoiceZipcode;

    @OneToMany(mappedBy = "customer")
    @LazyCollection(LazyCollectionOption.FALSE)
    @EqualsAndHashCode.Exclude
    private Set<CustomerSubject> subjects;


    //@ManyToMany(mappedBy = "customers")
    //@LazyCollection(LazyCollectionOption.TRUE)
    //private Collection<Tutor> tutors;

    /**
     * Adds a subject the customer learns
     * @param subject subject to add
     * @param quota quota of subject, null if unlimited
     */
    public void addSubject(Subject subject, Integer quota) {
        if(subjects == null)
            subjects = new HashSet<>();

        subjects.add(new CustomerSubject(this, subject, quota));
    }
    public void deleteSubject(CustomerSubject customerSubject){
        if(subjects == null) return;

        subjects.remove(customerSubject);
    };

    @Override
    public String toString() {
        return getFullName();
    }

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
