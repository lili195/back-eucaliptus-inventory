package com.eucaliptus.springboot_app_person.model;

import com.eucaliptus.springboot_app_person.enums.EnumRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "companies")
public class Company{


    @Id
    @Column(name = "nit_company")
    private String nitCompany;

    @Column(name = "name_company")
    private String nameCompany;

    @Column(name = "email_company")
    private String emailCompany;

    @Column(name = "phone_number_company")
    private String phoneNumberCompany;

    @Column(name = "address_company")
    private String addressCompany;

    public Company(String nitCompany, String name, String email, String phone, String address){
        this.nitCompany = nitCompany;
        this.nameCompany = name;
        this.emailCompany = email;
        this.phoneNumberCompany = phone;
        this.addressCompany = address;
    }

}
