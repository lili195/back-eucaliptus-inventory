package com.eucaliptus.springboot_app_person.model;

import com.eucaliptus.springboot_app_person.enums.EnumDocumentType;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "document_types")
public class DocumentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_document_type")
    private Long idDocumentType;

    @Column(name = "name_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumDocumentType nameType;

    public DocumentType(@Nonnull EnumDocumentType nameType) {
        this.nameType = nameType;
    }

}
