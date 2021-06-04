package de.unisiegen.propra.groupfour.braingainmanagement.data.service;

import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Tutor;
import de.unisiegen.propra.groupfour.braingainmanagement.data.repository.TutorRepository;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TutorService extends CrudService<Tutor, UUID> {

    @Getter(AccessLevel.PROTECTED)
    private final TutorRepository repository;

    public TutorService(@Autowired TutorRepository repository) {
        this.repository = repository;
    }

    public List<Tutor> fetchAll() {
        final List<Tutor> tutors = repository.findAll();
        tutors.forEach(t -> System.out.printf("%s, %s %s\n", t.getId(), t.getPrename(), t.getSurname()));
        return tutors;
    }

    @Override
    public Optional<Tutor> get(final UUID uuid) {
        return repository.findAll().stream().filter(t -> t.getId().equals(uuid)).findFirst();
    }

}
