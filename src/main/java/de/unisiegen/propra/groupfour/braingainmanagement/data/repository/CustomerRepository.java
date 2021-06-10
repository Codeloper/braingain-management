package de.unisiegen.propra.groupfour.braingainmanagement.data.repository;

import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
