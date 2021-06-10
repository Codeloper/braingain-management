package de.unisiegen.propra.groupfour.braingainmanagement.data.repository;

import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {
}
