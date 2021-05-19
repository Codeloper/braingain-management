package de.unisiegen.propra.groupfour.braingainmanagement.data.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Collection;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Tutor extends Person {

    @Column(nullable = false)
    private String bic, iban;

    @OneToMany
    private Collection<Subject> subjects;

}
