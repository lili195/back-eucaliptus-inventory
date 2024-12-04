package com.eucaliptus.springboot_app_person.model;

import com.eucaliptus.springboot_app_person.enums.EnumDocumentType;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa una entidad {@link DocumentType} que corresponde a la tabla 'document_types' en la base de datos.
 * Esta clase se utiliza para mapear el tipo de documento, asociado a un identificador único (ID).
 * Los tipos de documentos son representados a través de un valor enumerado {@link EnumDocumentType}.
 *
 * La clase está asociada con la tabla 'document_types' y contiene un identificador único y un tipo de documento,
 * que se almacenan en la base de datos.
 */

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "document_types")
public class DocumentType {

    /**
     * El identificador único del tipo de documento.
     * Este campo es generado automáticamente por la base de datos con una estrategia de incremento automático.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_document_type")
    private Long idDocumentType;

    /**
     * El nombre del tipo de documento, representado mediante el valor de un {@link EnumDocumentType}.
     * Este campo no puede ser nulo y se mapea a la columna 'name_type' en la base de datos.
     */

    @Column(name = "name_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumDocumentType nameType;

    /**
     * Constructor de la clase {@link DocumentType} que inicializa el tipo de documento.
     *
     * @param nameType El tipo de documento, representado por un valor del enumerado {@link EnumDocumentType}.
     */

    public DocumentType(@Nonnull EnumDocumentType nameType) {
        this.nameType = nameType;
    }

}
