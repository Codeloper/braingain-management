package de.unisiegen.propra.groupfour.braingainmanagement.data.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class Person {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(nullable = false, updatable = false, columnDefinition = "CHAR(36)")
    private UUID id;

    @Column(nullable = false)
    private String prename, surname;

    @Column(unique = true)
    private String phone, email;

    @Column(nullable = false)
    private String street, city;

    @Column(nullable = false)
    private Integer zipcode;

    @OneToOne(mappedBy = "person")
    @PrimaryKeyJoinColumn
    private User user;

}
