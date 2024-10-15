package com.eucaliptus.springboot_app_person.model;

import jakarta.persistence.*;

@Entity
@Table(name = "companies")
public class Company {

    @Id
    @Column(name = "nit_company")
    private String nitCompany;

    @Column(name = "name_company", nullable = false)
    private String nameCompany;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    // Getters y Setters
}
