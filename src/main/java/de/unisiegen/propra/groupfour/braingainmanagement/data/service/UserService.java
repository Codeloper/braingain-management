package de.unisiegen.propra.groupfour.braingainmanagement.data.service;

import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.Tutor;
import de.unisiegen.propra.groupfour.braingainmanagement.data.entity.User;
import de.unisiegen.propra.groupfour.braingainmanagement.data.repository.TutorRepository;
import de.unisiegen.propra.groupfour.braingainmanagement.data.repository.UserRepository;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.List;
import java.util.UUID;

@Service
public class UserService extends CrudService<User, String> {
    @Getter(AccessLevel.PROTECTED)
    private final UserRepository repository;

    public UserService(@Autowired UserRepository repository) {
        this.repository = repository;
    }

    public List<User> fetchAll() {
        return (List<User>) repository.findAll();
    }
}
