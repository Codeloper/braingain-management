package de.unisiegen.propra.groupfour.braingainmanagement.data.repository;

import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Tutor;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TutorRepository extends CrudRepository<Tutor, UUID> {
}
