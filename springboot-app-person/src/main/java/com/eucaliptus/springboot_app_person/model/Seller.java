package com.eucaliptus.springboot_app_person.model;


import com.eucaliptus.springboot_app_person.enums.EnumRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa un vendedor en el sistema.
 *
 * La clase {@link Seller} es una entidad JPA que mapea la tabla 'sellers'. Extiende la clase {@link Person}
 * y agrega un atributo específico para el nombre de usuario del vendedor. El rol de la persona es fijo como
 * {@link EnumRole#ROLE_SELLER}.
 *
 * Esta clase hereda los atributos comunes de una persona y agrega el atributo 'username', que es único para cada vendedor.
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "sellers")
//@DiscriminatorValue("ROLE_SELLER")
public class Seller extends Person{

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    /**
     * Constructor para crear un vendedor con todos los atributos inicializados, incluidos los detalles del vendedor
     * y el nombre de usuario.
     *
     * @param idNumber El número de identificación del vendedor.
     * @param firstName El primer nombre del vendedor.
     * @param lastName El apellido del vendedor.
     * @param email El correo electrónico del vendedor.
     * @param address La dirección del vendedor.
     * @param phoneNumber El número de teléfono del vendedor.
     * @param documentType El tipo de documento asociado con el vendedor.
     * @param username El nombre de usuario único del vendedor.
     */

    public Seller(String idNumber, String firstName, String lastName, String email, String address, String phoneNumber, DocumentType documentType,
                  String username) {
        super(idNumber, firstName, lastName, email, address, phoneNumber, documentType, EnumRole.ROLE_SELLER);
        this.username = username;
    }

    /**
     * Constructor para crear un vendedor con solo los atributos básicos de la persona, sin el nombre de usuario.
     * Este constructor puede ser usado cuando no se dispone de esa información en el momento de la creación del vendedor.
     *
     * @param idNumber El número de identificación del vendedor.
     * @param firstName El primer nombre del vendedor.
     * @param lastName El apellido del vendedor.
     * @param email El correo electrónico del vendedor.
     * @param address La dirección del vendedor.
     * @param phoneNumber El número de teléfono del vendedor.
     * @param documentType El tipo de documento asociado con el vendedor.
     */

    public Seller(String idNumber, String firstName, String lastName, String email, String address, String phoneNumber, DocumentType documentType) {
        super(idNumber, firstName, lastName, email, address, phoneNumber, documentType, EnumRole.ROLE_SELLER);
    }
}
