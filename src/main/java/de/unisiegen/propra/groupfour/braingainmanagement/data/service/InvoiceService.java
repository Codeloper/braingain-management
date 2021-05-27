package de.unisiegen.propra.groupfour.braingainmanagement.data.service;

import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.*;
import de.unisiegen.propra.groupfour.braingainmanagement.data.repository.InvoiceRepository;
import de.unisiegen.propra.groupfour.braingainmanagement.util.exception.LessonsAlreadyInvoicedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository repository;

    /**
     * Creates a new invoice. Generates a unique id by date in format "YYYYMMDD*", e.g. "202105281"
     * @param recipient recipient of invoice
     * @param lessons Collection of lessons
     * @return new invoice
     */
    public Invoice createInvoice(final Person recipient, final Collection<Lesson> lessons) {
        final Invoice invoice = new Invoice();
        invoice.setDate(LocalDate.now());
        invoice.setId(generateInvoiceId(invoice.getDate()));
        invoice.setRecipient(recipient);

        final Stream<Lesson> stream = recipient instanceof Customer ? lessons.stream().filter(Lesson::isInvoicedToCustomer) : recipient instanceof Tutor ? lessons.stream().filter(Lesson::isInvoicedByTutor) : null;
        if(stream == null)
            throw new IllegalStateException("Person has to be Customer or Tutor");

        if(stream.count() > 0)
            throw new LessonsAlreadyInvoicedException(stream.collect(Collectors.toSet()));

        repository.save(invoice);
        return invoice;
    }

    /**
     * Generates a unique invoice id in format "YYYYMMDD*", e.g. "202105281"
     * @param date date to create invoice id from
     * @return unique invoice id
     */
    private String generateInvoiceId(final LocalDate date) {
        final String dateString = date.format(DateTimeFormatter.BASIC_ISO_DATE);
        return dateString + Integer.parseInt(repository.findFirstByIdStartsWithOrderByIdDesc(dateString).getId().replace(dateString, "")) + 1;
    }

}
