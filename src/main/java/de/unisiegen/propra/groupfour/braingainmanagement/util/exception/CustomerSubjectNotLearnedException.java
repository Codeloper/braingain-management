package de.unisiegen.propra.groupfour.braingainmanagement.util.exception;

import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Customer;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Subject;
import lombok.Getter;

/**
 * Appears if attempted to book a lesson with a subject the customer is not learning
 */
public class CustomerSubjectNotLearnedException extends RuntimeException {

    @Getter
    private final Customer customer;

    @Getter
    private final Subject subject;

    public CustomerSubjectNotLearnedException(Customer customer, Subject subject) {
        super(String.format("Customer %s is not taught in subject %s", customer, subject));
        this.customer = customer;
        this.subject = subject;
    }

}
