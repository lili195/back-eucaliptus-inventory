package com.eucaliptus.springboot_app_person.model;

import com.eucaliptus.springboot_app_person.enums.EnumRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase abstracta que representa una persona en el sistema.
 *
 * La clase {@link Person} es una entidad JPA que se mapea a la tabla 'persons'.
 * Está diseñada como clase base para otros tipos de personas, como vendedores, proveedores, etc.
 * La herencia se maneja mediante la estrategia de herencia {@link InheritanceType#JOINED}, lo que significa que
 * cada subclase tendrá su propia tabla, y las propiedades comunes a todas las personas se almacenan en esta tabla base.
 *
 * Los atributos incluyen la información básica de la persona como número de identificación, nombre, apellido, correo electrónico,
 * dirección, teléfono, tipo de documento, rol y estado activo.
 */

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "persons")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person {

    /**
     * El número de identificación de la persona. Este campo es la clave primaria.
     */

    @Id
    @Column(name = "id_number")
    private String idNumber;

    /**
     * El primer nombre de la persona. Este campo es obligatorio (no puede ser nulo).
     */

    @Column(name = "first_name", nullable = false)
    private String firstName;

    /**
     * El apellido de la persona. Este campo es opcional.
     */

    @Column(name = "last_name")
    private String lastName;

    /**
     * El correo electrónico de la persona. Este campo es opcional.
     */

    @Column(name = "email")
    private String email;

    /**
     * La dirección de la persona. Este campo es opcional.
     */

    @Column(name = "address")
    private String address;

    /**
     * El número de teléfono de la persona. Este campo es opcional.
     */

    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * El tipo de documento de la persona. Este campo está relacionado con la entidad {@link DocumentType}.
     * Se mapea a la columna 'id_document_type' en la base de datos.
     */

    @ManyToOne
    @JoinColumn(name = "id_document_type", referencedColumnName = "id_document_type")
    private DocumentType documentType;

    /**
     * Indica si la persona está activa en el sistema. El valor predeterminado es {@code true}.
     */

    @Column(name = "active")
    private boolean active;

    /**
     * El rol de la persona, representado por un valor del enumerado {@link EnumRole}.
     * Este campo es obligatorio (no puede ser nulo).
     */

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumRole role;

    /**
     * Constructor de la clase {@link Person} que inicializa los atributos básicos de una persona.
     *
     * @param idNumber El número de identificación de la persona.
     * @param firstName El primer nombre de la persona.
     * @param lastName El apellido de la persona.
     * @param email El correo electrónico de la persona.
     * @param address La dirección de la persona.
     * @param phoneNumber El número de teléfono de la persona.
     * @param documentType El tipo de documento asociado con la persona.
     * @param role El rol de la persona, representado por un valor del enumerado {@link EnumRole}.
     */

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
