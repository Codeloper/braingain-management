package de.unisiegen.propra.groupfour.braingainmanagement.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subject {

    @Id
    @Column(nullable = false, updatable = false)
    private String id;

    @Column(nullable = false)
    private Integer tutorFee, customerPrice;

    @Override
    public String toString() {
        return id;
    }

}
