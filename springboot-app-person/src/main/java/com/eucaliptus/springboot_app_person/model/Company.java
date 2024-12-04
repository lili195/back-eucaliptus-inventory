package com.eucaliptus.springboot_app_person.model;

import com.eucaliptus.springboot_app_person.enums.EnumRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa una entidad {@link Company} que corresponde a la tabla 'companies' en la base de datos.
 * Esta clase es utilizada para mapear los datos de una compañía, incluyendo su NIT, nombre, correo electrónico,
 * número de teléfono y dirección.
 *
 * La clase utiliza anotaciones JPA para la persistencia de datos en la base de datos, y la misma está asociada
 * a la tabla 'companies' donde se almacenan estos detalles.
 */

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "companies")
public class Company{

    /**
     * El NIT de la compañía, que es un identificador único utilizado para las empresas en ciertos países.
     * Este campo está marcado como clave primaria de la tabla.
     */

    @Id
    @Column(name = "nit_company")
    private String nitCompany;

    /**
     * El nombre de la compañía.
     */

    @Column(name = "name_company")
    private String nameCompany;

    /**
     * El correo electrónico de la compañía.
     */

    @Column(name = "email_company")
    private String emailCompany;

    /**
     * El número de teléfono de la compañía.
     */

    @Column(name = "phone_number_company")
    private String phoneNumberCompany;

    /**
     * La dirección de la compañía.
     */

    @Column(name = "address_company")
    private String addressCompany;

    /**
     * Constructor de la clase {@link Company} que inicializa los atributos de la compañía.
     *
     * @param nitCompany El NIT de la compañía.
     * @param name El nombre de la compañía.
     * @param email El correo electrónico de la compañía.
     * @param phone El número de teléfono de la compañía.
     * @param address La dirección de la compañía.
     */

    public Company(String nitCompany, String name, String email, String phone, String address){
        this.nitCompany = nitCompany;
        this.nameCompany = name;
        this.emailCompany = email;
        this.phoneNumberCompany = phone;
        this.addressCompany = address;
    }

}
