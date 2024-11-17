package com.eucaliptus.springboot_app_person.model;

import com.eucaliptus.springboot_app_person.enums.EnumRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "persons")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person {

    @Id
    @Column(name = "id_number")
    private String idNumber;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "id_document_type", referencedColumnName = "id_document_type")
    private DocumentType documentType;

    @Column(name = "active")
    private boolean active;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumRole role;

    public Person(String idNumber, String firstName, String lastName, String email, String address, String phoneNumber, DocumentType documentType, EnumRole role) {
        this.idNumber = idNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.documentType = documentType;
        this.role = role;
        this.active = true;
    }
}
