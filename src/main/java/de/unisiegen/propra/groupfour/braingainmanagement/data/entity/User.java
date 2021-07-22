package de.unisiegen.propra.groupfour.braingainmanagement.data.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

@Entity
@Data
public class User implements UserDetails {

    @Id
    @Column(nullable = false, updatable = true)
    private String email;

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Person person;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false, columnDefinition = "ENUM('TUTOR', 'ADMIN')")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public enum Role {
        TUTOR, ADMIN;
    }

}
