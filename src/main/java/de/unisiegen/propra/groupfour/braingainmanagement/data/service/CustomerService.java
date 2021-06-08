package de.unisiegen.propra.groupfour.braingainmanagement.data.service;

import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Customer;
import de.unisiegen.propra.groupfour.braingainmanagement.data.repository.CustomerRepository;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService extends CrudService<Customer, UUID> {

    @Getter(AccessLevel.PROTECTED)
    private final CustomerRepository repository;

    public CustomerService(@Autowired CustomerRepository repository) { this.repository = repository; }

    public List<Customer> fetchAll() {
        final List<Customer> customers = repository.findAll();
        customers.forEach(t -> System.out.printf("%s, %s %s\n", t.getId(), t.getPrename(), t.getSurname()));
        return customers;
    }

    @Override
    public Optional<Customer> get(final UUID uuid) {
        return repository.findAll().stream().filter(t -> t.getId().equals(uuid)).findFirst();
    }
}
