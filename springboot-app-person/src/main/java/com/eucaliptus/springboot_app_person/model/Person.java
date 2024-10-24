package com.eucaliptus.springboot_app_person.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "persons")
public class Person {

    @Id
    @Column(name = "id_number")
    private String idNumber;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "id_document_type", referencedColumnName = "id_document_type")
    private DocumentType documentType;

    @ManyToOne
    @JoinColumn(name = "id_role", referencedColumnName = "id_role")
    private Role role;

    @Column(name = "active")
    private boolean active;

    public Person(String idNumber, String firstName, String lastName, String email, String phoneNumber, DocumentType documentType, Role role) {
        this.idNumber = idNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.documentType = documentType;
        this.role = role;
        this.active = true;
    }
}
