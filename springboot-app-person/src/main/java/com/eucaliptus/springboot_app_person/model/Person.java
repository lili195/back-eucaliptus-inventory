package com.eucaliptus.springboot_app_person.model;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class Person {

    @Id
    @Column(name = "id_number")
    private Long idNumber;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "id_document_type", referencedColumnName = "id_document_type")
    private DocumentType documentType;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "home_address")
    private String homeAddress;

    @ManyToOne
    @JoinColumn(name = "id_role", referencedColumnName = "id_role")
    private Role role;

    // getters and setters

    public String getFirstName() {
        return "";
    }

    public String getLastName() {
        return "";
    }

    public String getPhoneNumber() {
        return "";
    }

    public String getHomeAddress() {
        return "";
    }


    public int getDocumentType() {
        return 0;
    }

    public int getRole() {
        return 0;
    }

    public void setFirstName(String firstName) {
    }


    public void setLastName(String lastName) {
    }

    public void setPhoneNumber(String phoneNumber) {
    }

    public void setHomeAddress(String homeAddress) {
    }

    public void setDocumentType(int documentType) {
    }

    public void setRole(int role) {
    }
}
