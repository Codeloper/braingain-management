package de.unisiegen.propra.groupfour.braingainmanagement.data.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;

@Entity
@Data
public class Invoice {

    @Id
    @Column(nullable = false, updatable = false)
    private String id;

    @Column(nullable = false, updatable = false)
    private LocalDate date;

    @ManyToOne
    private Person recipient;

    @ManyToMany
    private Collection<Lesson> lessons;

}
