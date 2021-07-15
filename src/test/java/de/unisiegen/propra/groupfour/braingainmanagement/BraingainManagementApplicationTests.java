package de.unisiegen.propra.groupfour.braingainmanagement;

import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.*;
import de.unisiegen.propra.groupfour.braingainmanagement.data.repository.CustomerRepository;
import de.unisiegen.propra.groupfour.braingainmanagement.data.repository.SubjectRepository;
import de.unisiegen.propra.groupfour.braingainmanagement.data.repository.TutorRepository;
import de.unisiegen.propra.groupfour.braingainmanagement.data.service.InvoicePdfService;
import de.unisiegen.propra.groupfour.braingainmanagement.data.service.TutorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@SpringBootTest
class BraingainManagementApplicationTests {

	@Autowired
	private TutorRepository tutorRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private TutorService tutorService;

	@Autowired
	private InvoicePdfService invoicePdfService;

	@Test
	void contextLoads() {
	}

	@Test
	void fillDatabase() {
		Assertions.assertDoesNotThrow(() -> {
			final Tutor tutorTom = new Tutor("Tom", "Rasch", "01712693653", "me@tom.cologne", "Westfälische Straße 62", "Olpe", "57462", "WELADED1OPE", "DE48462500490002574473");
			final Tutor tutorRainer = new Tutor("Rainer", "Zufall", "23489234", "rainerzufall@icloud.com", "Wasdasd 62", "Olpe", "57462", "asjknd", "234234");
			final Customer customerPeter = new Customer("Peter", "Lustig", "035915318888", "peterlustig@icloud.com", "Musterstraße 2", "Olpe", "57462");
			final Customer customerEmilia = new Customer("Emilia", "Lustig", "03591531888348", "emilialustig@icloud.com", "Musterstraße 2", "Olpe", "57462");
			final Subject subjectMath = new Subject("Mathe", 25, 15);
			final Subject subjectGerman = new Subject("Deutsch", 25, 15);
			final Subject subjectEnglish = new Subject("Englisch", 25, 15);

			customerPeter.addSubject(subjectMath, 30);
			customerPeter.addSubject(subjectGerman, 30);
			customerEmilia.addSubject(subjectEnglish, null);
			tutorTom.addSubject(subjectMath);
			tutorRainer.addSubject(subjectGerman);
			tutorRainer.addSubject(subjectEnglish);

			subjectRepository.saveAll(Arrays.asList(subjectMath, subjectEnglish, subjectGerman));
			customerRepository.saveAll(Arrays.asList(customerEmilia, customerPeter));
			tutorRepository.saveAll(Arrays.asList(tutorRainer, tutorTom));
		});
	}

	@Test
	void tutorService() {
		//tutorService.update(new Tutor("Tom", "Rasch", "1", "aa", "aa", "a", "a", "a", "a"));
		//Assertions.assertEquals("a199518d-3267-4003-a7f5-4fa636280341, Tom Rasch\n", tutorService.fetchAll().stream().map(t -> String.format("%s, %s %s\n", t.getId(), t.getPrename(), t.getSurname())).collect(Collectors.joining()));
		//Assertions.assertTrue(tutorService.getRepository().findById(UUID.fromString("a199518d-3267-4003-a7f5-4fa636280341")).isPresent());
	}

	@Test
	void pdfCreate() {
		final Invoice invoice = new Invoice();
		final Customer customer = new Customer("Peter", "Lustig", "51561650", "asd@agm.de", "Musterstraße 2", "Olpe", "57462");
		final Tutor tutor = new Tutor("Tom", "Rasch", "01712693653", "me@tom.cologne", "Westfälische Straße 62", "Olpe", "57462", "WELADED1OPE", "DE48462500490002574473");
		final Subject subject = new Subject("Mathe", 15, 25);
		invoice.setDate(LocalDate.now());
		invoice.setId("2020060801");
		invoice.setRecipient(customer);
		invoice.setLessons(Arrays.asList(new Lesson(LocalDate.now(), 2.0, tutor, customer, subject), new Lesson(LocalDate.now(), 1.0, tutor, customer, subject)));
		try {
			invoicePdfService.createPdf(invoice);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
