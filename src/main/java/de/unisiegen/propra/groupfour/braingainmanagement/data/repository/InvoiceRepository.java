package de.unisiegen.propra.groupfour.braingainmanagement.data.repository;

import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Invoice;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface InvoiceRepository extends CrudRepository<Invoice, String> {

    Invoice findFirstByIdStartsWithOrderByIdDesc(String id);

}
