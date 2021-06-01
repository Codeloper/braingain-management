package de.unisiegen.propra.groupfour.braingainmanagement.util.exception;

import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.CustomerSubject;
import lombok.Getter;

/**
 * Appears if attempted to book a lesson which would exceed the customers quota of this subject
 */
public class CustomerSubjectQuotaExceededException extends RuntimeException {

    @Getter
    private final CustomerSubject customerSubject;

    public CustomerSubjectQuotaExceededException(CustomerSubject customerSubject) {
        super(String.format("Customer %s exceeds his quota %d for subject %s", customerSubject.getCustomer(), customerSubject.getQuota(), customerSubject.getSubject()));
        this.customerSubject = customerSubject;
    }

}
