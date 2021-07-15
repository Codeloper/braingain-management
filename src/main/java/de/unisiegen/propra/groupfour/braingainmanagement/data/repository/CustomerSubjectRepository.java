package de.unisiegen.propra.groupfour.braingainmanagement.data.repository;

import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Customer;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.CustomerSubject;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.CustomerSubjectKey;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface CustomerSubjectRepository extends CrudRepository<CustomerSubject, CustomerSubjectKey> {
    Set<CustomerSubject> findAllByCustomerEquals(Customer customer);
}
