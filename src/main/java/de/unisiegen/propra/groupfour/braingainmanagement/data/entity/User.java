package de.unisiegen.propra.groupfour.braingainmanagement.data.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class User {

    @Id
    @Column(nullable = false, updatable = false)
    private String email;

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    private Person person;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false, columnDefinition = "ENUM('TUTOR', 'ADMIN')")
    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        TUTOR, ADMIN;
    }

}
