package de.unisiegen.propra.groupfour.braingainmanagement.data.service;

import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.*;
import de.unisiegen.propra.groupfour.braingainmanagement.data.repository.InvoiceRepository;
import de.unisiegen.propra.groupfour.braingainmanagement.util.exception.LessonAlreadyInvoicedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository repository;

    /**
     * Creates a new invoice. Generates a unique id by date in format "YYYYMMDD*", e.g. "202105281".
     * @param recipient recipient of invoice
     * @param lessons Collection of lessons
     * @throws LessonAlreadyInvoicedException Thrown if any of the given lessons is already invoiced
     * @return New invoice
     */
    public Invoice createInvoice(Person recipient, Collection<Lesson> lessons) {
        final Invoice invoice = new Invoice();
        invoice.setDate(LocalDate.now());
        invoice.setId(generateInvoiceId(invoice.getDate()));
        invoice.setRecipient(recipient);

        final Collection<Lesson> invoicedLessons = recipient instanceof Customer ? lessons.stream().filter(Lesson::isInvoicedToCustomer).collect(Collectors.toSet()) : recipient instanceof Tutor ? lessons.stream().filter(Lesson::isInvoicedByTutor).collect(Collectors.toSet()) : null;
        if(invoicedLessons == null)
            throw new IllegalStateException("recipient has to be Customer or Tutor");

        if(invoicedLessons.size() > 0)
            throw new LessonAlreadyInvoicedException(lessons);

        invoice.setLessons(lessons);

        repository.save(invoice);

        return invoice;
    }

    /**
     * Generates a unique invoice id in format "YYYYMMDD*", e.g. "202105281"
     * @param date date to create invoice id from
     * @return unique invoice id
     */
    private String generateInvoiceId(LocalDate date) {
        final String dateString = date.format(DateTimeFormatter.BASIC_ISO_DATE);
        final Optional<Invoice> lastInvoice = repository.findFirstByIdStartsWithOrderByIdDesc(dateString);
        return dateString + lastInvoice.map(invoice -> Integer.parseInt(invoice.getId().replace(dateString, "")) + 1).orElse(1);
    }

}
