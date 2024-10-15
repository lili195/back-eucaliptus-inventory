package com.eucaliptus.springboot_app_person.model;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role")
    private Long idRole;

    @Column(name = "name_role", nullable = false)
    private String nameRole;

    public Object getNameRole() {
        return null;
    }

    public void setNameRole(Object nameRole) {

    }

    // Getters y Setters
}
