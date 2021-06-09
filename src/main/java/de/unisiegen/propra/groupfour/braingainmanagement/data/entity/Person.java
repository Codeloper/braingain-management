package de.unisiegen.propra.groupfour.braingainmanagement.data.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
public abstract class Person {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(nullable = false, updatable = false/*, columnDefinition = "CHAR(36)"*/)
    private UUID id;

    @Column(nullable = false)
    private String prename, surname;

    @Column(unique = true)
    private String phone, email;

    @Column(nullable = false)
    private String street, city;

    @Column(nullable = false, columnDefinition = "CHAR(5)")
    private String zipcode;

    @OneToOne(mappedBy = "person")
    @PrimaryKeyJoinColumn
    private User user;

    public Person(String prename, String surname, String phone, String email, String street, String city, String zipcode) {
        this.prename = prename;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.street = street;
        this.city = city;
        this.zipcode = zipcode;
    }

    public String getFullName() {
        return prename + " " + surname;
    }

}
