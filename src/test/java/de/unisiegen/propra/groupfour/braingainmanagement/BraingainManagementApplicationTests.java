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

			//subjectRepository.saveAll(Arrays.asList(subjectMath, subjectEnglish, subjectGerman));
			//customerRepository.saveAll(Arrays.asList(customerEmilia, customerPeter));
			//tutorRepository.saveAll(Arrays.asList(tutorRainer, tutorTom));
		});
	}

}
