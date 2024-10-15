package com.eucaliptus.springboot_app_person.model;

import jakarta.persistence.*;

@Entity
@Table(name = "document_types")
public class DocumentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_document_type")
    private Long idDocumentType;

    @Column(name = "name_type", nullable = false)
    private String nameType;

    public Object getNameType() {
        return null;
    }

    public void setNameType(Object nameType) {
    }

    // Getters y Setters
}
