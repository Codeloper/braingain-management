package de.unisiegen.propra.groupfour.braingainmanagement.data.repository;

import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Customer;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Lesson;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Subject;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.Month;
import java.util.UUID;

public interface LessonRepository extends CrudRepository<Lesson, UUID> {

    @Query("select count(l) from Lesson l where l.customer = :customer and l.subject = :subject and month(l.date) = :month")
    int countCustomerSubjectQuota(Customer customer, Subject subject, int month);

}
