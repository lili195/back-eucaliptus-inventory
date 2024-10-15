package com.eucaliptus.springboot_app_person.model;

import jakarta.persistence.*;

@Entity
@Table(name = "providers")
public class Provider extends Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_provider")
    private Long idProvider;

    @Column(name = "person_type", nullable = false)
    private String personType;

    @ManyToOne(optional = true)
    @JoinColumn(name = "nit_company", referencedColumnName = "nit_company")
    private Company company;

    public Object getPersonType() {
        return null;
    }

    public Object getCompany() {
        return null;
    }

    public void setPersonType(Object personType) {
    }

    public void setCompany(Object company) {
    }

    // Getters y Setters
}
