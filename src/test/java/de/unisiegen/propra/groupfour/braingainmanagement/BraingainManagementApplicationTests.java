package de.unisiegen.propra.groupfour.braingainmanagement;

import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.*;
import de.unisiegen.propra.groupfour.braingainmanagement.data.service.InvoicePdfService;
import de.unisiegen.propra.groupfour.braingainmanagement.data.service.TutorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.stream.Collectors;

@SpringBootTest
class BraingainManagementApplicationTests {

	@Autowired
	private TutorService tutorService;

	@Autowired
	private InvoicePdfService invoicePdfService;

	@Test
	void contextLoads() {
	}

	@Test
	void tutorService() {
		//tutorService.update(new Tutor("Tom", "Rasch", "1", "aa", "aa", "a", "a", "a", "a"));
		Assertions.assertEquals("ba14c1ff-d0a7-46b3-ba8b-a50a9a813345, Tom Rasch", tutorService.fetchAll().stream().map(t -> String.format("%s, %s %s", t.getId(), t.getPrename(), t.getSurname())).collect(Collectors.joining()));
	}

	@Test
	void pdfCreate() {
		final Invoice invoice = new Invoice();
		final Customer customer = new Customer("Peter", "Lustig", "51561650", "asd@agm.de", "Musterstraße 2", "Olpe", "57462");
		final Tutor tutor = new Tutor("Tom", "Rasch", "01712693653", "me@tom.cologne", "Westfälische Straße 62", "Olpe", "57462", "WELADED1OPE", "DE48462500490002574473");
		final Subject subject = new Subject();
		subject.setId("Informatik");
		subject.setCustomerPrice(25);
		subject.setTutorFee(25);
		invoice.setDate(LocalDate.now());
		invoice.setId("2020060801");
		invoice.setRecipient(customer);
		invoice.setLessons(Arrays.asList(new Lesson(LocalDate.now(), 2, tutor, customer, subject), new Lesson(LocalDate.now(), 1, tutor, customer, subject)));
		try {
			invoicePdfService.createCustomerInvoice(invoice);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
