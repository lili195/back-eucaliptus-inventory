package com.eucaliptus.springboot_app_person.model;

import com.eucaliptus.springboot_app_person.enums.EnumPersonType;
import com.eucaliptus.springboot_app_person.enums.EnumRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa un proveedor en el sistema.
 *
 * La clase {@link Provider} es una entidad JPA que mapea la tabla 'providers'. Extiende la clase {@link Person}
 * y agrega atributos específicos relacionados con los proveedores, como el nombre del banco, el número de cuenta bancaria,
 * el tipo de persona y la empresa asociada (si aplica).
 *
 * Esta clase hereda los atributos comunes de una persona y agrega información adicional relacionada con los proveedores.
 * El rol de la persona es fijo como {@link EnumRole#ROLE_PROVIDER}.
 */

@Entity
@NoArgsConstructor
@Setter
@Getter
@Table(name = "providers")
public class Provider extends Person{

    /**
     * El nombre del banco donde el proveedor tiene su cuenta.
     */
    @Column(name = "bank_name")
    private String bankName;

    /**
     * El número de cuenta bancaria del proveedor.
     */
    @Column(name = "bank_account_number")
    private String bankAccountNumber;

    /**
     * El tipo de persona del proveedor, representado por un valor del enumerado {@link EnumPersonType}.
     * Este campo es obligatorio (no puede ser nulo).
     */
    @Column(name = "person_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumPersonType personType;

    /**
     * La empresa asociada al proveedor, si aplica. Este campo puede ser nulo.
     * Se mapea a la columna 'nit_company' en la tabla 'companies'.
     */
    @ManyToOne(optional = true)
    @JoinColumn(name = "nit_company", referencedColumnName = "nit_company")
    private Company company;

    /**
     * Constructor para crear un proveedor con todos los atributos inicializados, incluidos los detalles bancarios
     * y la empresa asociada.
     *
     * @param idNumber El número de identificación del proveedor.
     * @param firstName El primer nombre del proveedor.
     * @param lastName El apellido del proveedor.
     * @param email El correo electrónico del proveedor.
     * @param address La dirección del proveedor.
     * @param phoneNumber El número de teléfono del proveedor.
     * @param documentType El tipo de documento asociado con el proveedor.
     * @param bankName El nombre del banco donde el proveedor tiene su cuenta.
     * @param bankAccountNumber El número de cuenta bancaria del proveedor.
     * @param personType El tipo de persona del proveedor, representado por un valor de {@link EnumPersonType}.
     * @param company La empresa asociada al proveedor, puede ser nula si no aplica.
     */

    public Provider (String idNumber, String firstName, String lastName, String email, String address, String phoneNumber, DocumentType documentType,
                     String bankName, String bankAccountNumber, EnumPersonType personType, Company company){
        super(idNumber, firstName, lastName, email, address, phoneNumber, documentType, EnumRole.ROLE_PROVIDER);
        this.bankName = bankName;
        this.bankAccountNumber = bankAccountNumber;
        this.personType = personType;
        this.company = company;
    }

    /**
     * Constructor para crear un proveedor con solo los atributos básicos de la persona, sin los detalles bancarios ni la empresa asociada.
     * Este constructor puede ser usado cuando no se dispone de esa información en el momento de la creación del proveedor.
     *
     * @param idNumber El número de identificación del proveedor.
     * @param firstName El primer nombre del proveedor.
     * @param lastName El apellido del proveedor.
     * @param email El correo electrónico del proveedor.
     * @param address La dirección del proveedor.
     * @param phoneNumber El número de teléfono del proveedor.
     * @param documentType El tipo de documento asociado con el proveedor.
     */

    public Provider (String idNumber, String firstName, String lastName, String email, String address, String phoneNumber, DocumentType documentType){
        super(idNumber, firstName, lastName, email, address, phoneNumber, documentType, EnumRole.ROLE_PROVIDER);
    }

}
