package de.unisiegen.propra.groupfour.braingainmanagement.util.exception;

import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Lesson;
import lombok.Getter;

import java.util.Collection;

public class LessonsAlreadyInvoicedException extends RuntimeException {

    @Getter
    private final Collection<Lesson> lessons;

    public LessonsAlreadyInvoicedException(Collection<Lesson> lessons) {
        super("Lessons were already invoiced: " + lessons);
        this.lessons = lessons;
    }

}
