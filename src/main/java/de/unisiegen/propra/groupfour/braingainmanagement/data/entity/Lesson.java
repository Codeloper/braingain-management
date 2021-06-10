package de.unisiegen.propra.groupfour.braingainmanagement.data.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Lesson implements Comparable<Lesson> {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Integer count;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tutor tutor;

    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @ManyToOne
    private Subject subject;

    @ManyToMany(mappedBy = "lessons", fetch = FetchType.LAZY)
    private Collection<Invoice> invoices;

    public Lesson(LocalDate date, Integer count, Tutor tutor, Customer customer, Subject subject) {
        this.date = date;
        this.count = count;
        this.tutor = tutor;
        this.customer = customer;
        this.subject = subject;
    }

    /**
     * Calculates total sum of lesson.
     * @return total sum of lesson, count * customerPrice
     */
    public double customerTotal() {
        return count * subject.getCustomerPrice();
    }

    /**
     * Calculates total sum of lesson.
     * @return total sum of lesson, count * tutorFee
     */
    public double tutorTotal(){ return count * subject.getTutorFee();}

    /**
     * Checks if a customer invoice already contains this lesson
     * @return Whether there is a customer invoice containing this lesson
     */
    public boolean isInvoicedToCustomer() {
        return invoices.stream().anyMatch(i -> i.getRecipient().equals(customer));
    }

    /**
     * Checks if a tutor invoice already contains this lesson
     * @return Whether there is a tutor invoice containing this lesson
     */
    public boolean isInvoicedByTutor() {
        return invoices.stream().anyMatch(i -> i.getRecipient().equals(tutor));
    }

    @Override
    public int compareTo(Lesson o) {
        return date.compareTo(o.date);
    }
}
