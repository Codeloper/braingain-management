package de.unisiegen.propra.groupfour.braingainmanagement.data.repository;

import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Customer;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Lesson;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Subject;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.UUID;

public interface LessonRepository extends JpaRepository<Lesson, UUID> {

    //@Query("select count(l) from Lesson l where l.customer = :customer and l.subject = :subject and month(l.date) = :month")
    //int countCustomerSubjectQuota(Customer customer, Subject subject, int month);

    Collection<Lesson> findAllByCustomerEqualsAndDateBetween(Customer customer, LocalDate start, LocalDate end);

    Collection<Lesson> findAllByTutorEqualsAndDateBetween(Tutor tutor, LocalDate start, LocalDate end);

}
