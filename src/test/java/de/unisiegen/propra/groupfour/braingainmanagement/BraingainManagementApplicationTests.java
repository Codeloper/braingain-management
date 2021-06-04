package de.unisiegen.propra.groupfour.braingainmanagement;

import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Tutor;
import de.unisiegen.propra.groupfour.braingainmanagement.data.service.TutorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Collectors;

@SpringBootTest
class BraingainManagementApplicationTests {

	@Autowired
	private TutorService tutorService;

	@Test
	void contextLoads() {
	}

	@Test
	void tutorService() {
		//tutorService.update(new Tutor("Tom", "Rasch", "1", "aa", "aa", "a", "a", "a", "a"));
		Assertions.assertEquals("ba14c1ff-d0a7-46b3-ba8b-a50a9a813345, Tom Rasch", tutorService.fetchAll().stream().map(t -> String.format("%s, %s %s", t.getId(), t.getPrename(), t.getSurname())).collect(Collectors.joining()));
	}

}
