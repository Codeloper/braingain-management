package de.unisiegen.propra.groupfour.braingainmanagement.data.repository;

import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Lesson;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface LessonRepository extends CrudRepository<Lesson, UUID> {
}
