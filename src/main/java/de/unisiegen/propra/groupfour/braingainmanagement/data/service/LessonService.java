package de.unisiegen.propra.groupfour.braingainmanagement.data.service;

import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.*;
import de.unisiegen.propra.groupfour.braingainmanagement.data.repository.LessonRepository;
import de.unisiegen.propra.groupfour.braingainmanagement.data.repository.TutorRepository;
import de.unisiegen.propra.groupfour.braingainmanagement.util.exception.CustomerSubjectNotLearnedException;
import de.unisiegen.propra.groupfour.braingainmanagement.util.exception.CustomerSubjectQuotaExceededException;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LessonService extends CrudService<Lesson, UUID> {

    @Getter(AccessLevel.PROTECTED)
    private final LessonRepository repository;

    /**
     * Books a new lesson.
     * @param date date of lesson
     * @param count count of lesson (e.g. 2 for double lesson)
     * @param tutor tutor of lesson
     * @param customer student of lesson
     * @param subject subject of lesson
     * @throws CustomerSubjectNotLearnedException Thrown if the customer is not learning this subject and doesn't have a quota
     * @throws CustomerSubjectQuotaExceededException Thrown if the customers quota for this subject is exceeded
     * @return created lesson
     */
    public Lesson bookLesson(LocalDate date, double count, Tutor tutor, Customer customer, Subject subject) {
        final Lesson lesson = new Lesson(date, count, tutor, customer, subject);
        final Optional<CustomerSubject> customerSubject = customer.getSubjects().stream().filter(s -> s.getSubject().equals(subject)).findAny();

        if(customerSubject.isEmpty())
            throw new CustomerSubjectNotLearnedException(customer, subject);

        if(customerSubject.get().getQuota() <= countLessonsInSubjectByCustomerInMonth(customer, subject, date.getMonth()))
            throw new CustomerSubjectQuotaExceededException(customerSubject.get());

        repository.save(lesson);

        return lesson;
    }

    /**
     * Counts hold lessons in given subject by customer in given month to calculate quota
     * @param customer student of lessons
     * @param subject subject of lessons
     * @param month month of lessons
     * @return number of matching lessons
     */

    //TODO noch abändern
    public int countLessonsInSubjectByCustomerInMonth(Customer customer, Subject subject, Month month) {
        return 0;//repository.countCustomerSubjectQuota(customer, subject, month.getValue());
    }

    public LessonService(@Autowired LessonRepository repository) {
        this.repository = repository;
    }

    public List<Lesson> fetchAll() {
        return repository.findAll();
    }

    public List<Lesson> fetchAllByTutor(Tutor tutor){
        return (List<Lesson>) repository.findAllByTutorEquals(tutor);

    }

    public List<Lesson> fetchAllBySubject(Subject subject){
        return (List<Lesson>) repository.findAllBySubjectEquals(subject);
    }
}
