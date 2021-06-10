package de.unisiegen.propra.groupfour.braingainmanagement.data.repository;

import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Invoice;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, String> {

    Optional<Invoice> findFirstByIdStartsWithOrderByIdDesc(String id);

    Collection<Invoice> findAllByRecipientEquals(Person recipient);

}
