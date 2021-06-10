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

    /**
     * Calculates total sum of invoice for customers.
     * @return sum of all lesson totals
     */
    public double customerInvoiceTotal() {
        return lessons.stream().mapToDouble(Lesson::customerTotal).sum();
    }

    /**
     * Calculates total sum of invoice for tutors.
     * @return sum of all lesson totals
     */
    public double tutorInvoiceTotal(){return lessons.stream().mapToDouble(Lesson::tutorTotal).sum();}

}
