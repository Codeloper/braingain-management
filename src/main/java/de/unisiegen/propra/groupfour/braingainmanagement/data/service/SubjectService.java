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
public class SubjectService extends CrudService<Subject, String> {

    @Getter(AccessLevel.PROTECTED)
    private final SubjectRepository repository;

    public SubjectService(@Autowired SubjectRepository repository) {
        this.repository = repository;
    }

    public List<Subject> fetchAll() {
        return repository.findAll();
    }
}
