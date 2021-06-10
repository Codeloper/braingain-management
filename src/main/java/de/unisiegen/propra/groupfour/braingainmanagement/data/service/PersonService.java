package de.unisiegen.propra.groupfour.braingainmanagement.data.service;

import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Person;
import de.unisiegen.propra.groupfour.braingainmanagement.data.repository.PersonRepository;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.List;
import java.util.UUID;

@Service
public class PersonService extends CrudService<Person, UUID> {

    @Getter(AccessLevel.PROTECTED)
    private final PersonRepository repository;

    public PersonService(@Autowired PersonRepository repository) { this.repository = repository; }

    public List<Person> fetchAll() {
        return repository.findAll();
    }

}
