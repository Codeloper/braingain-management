package de.unisiegen.propra.groupfour.braingainmanagement.data.service;

import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Subject;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Tutor;
import de.unisiegen.propra.groupfour.braingainmanagement.data.repository.SubjectRepository;
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
public class SubjectService extends CrudService<Subject, UUID> {

    @Getter(AccessLevel.PROTECTED)
    private final SubjectRepository repository;

    public SubjectService(@Autowired SubjectRepository repository) {
        this.repository = repository;
    }

    public List<Subject> fetchAll() {
        final List<Subject> subjects = repository.findAll();
        subjects.forEach(t -> System.out.printf("%s\n", t.getId()));
        return subjects;
    }

    @Override
    public Optional<Subject> get(final UUID uuid) {
        return repository.findAll().stream().filter(t -> t.getId().equals(uuid)).findFirst();
    }
}
