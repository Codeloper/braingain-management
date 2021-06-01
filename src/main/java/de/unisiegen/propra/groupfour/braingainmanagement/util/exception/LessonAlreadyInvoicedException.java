package de.unisiegen.propra.groupfour.braingainmanagement.util.exception;

import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Lesson;
import lombok.Getter;

import java.util.Collection;

/**
 * Appears if attempted to create an invoice with an alrady invoiced lesson
 */
public class LessonAlreadyInvoicedException extends RuntimeException {

    @Getter
    private final Collection<Lesson> lessons;

    public LessonAlreadyInvoicedException(Collection<Lesson> lessons) {
        super("Lesson(s) were already invoiced: " + lessons);
        this.lessons = lessons;
    }

}
