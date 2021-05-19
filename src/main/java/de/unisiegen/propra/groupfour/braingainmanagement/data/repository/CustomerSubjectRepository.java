package de.unisiegen.propra.groupfour.braingainmanagement.data.repository;

import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.CustomerSubject;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.CustomerSubjectKey;
import org.springframework.data.repository.CrudRepository;

public interface CustomerSubjectRepository extends CrudRepository<CustomerSubject, CustomerSubjectKey> {
}
