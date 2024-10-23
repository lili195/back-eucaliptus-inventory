package com.eucaliptus.springboot_app_person.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "companies")
public class Company {

    @Id
    @Column(name = "nit_company")
    private String nitCompany;

    @Column(name = "name_company")
    private String nameCompany;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_account_number")
    private String bankAccountNumber;

    public Company(@Nonnull String nitCompany, @Nonnull String nameCompany, @Nonnull String phoneNumber,
                   @Nonnull String email, @Nonnull String address, @Nonnull String bankName, @Nonnull String bankAccountNumber) {
        this.nitCompany = nitCompany;
        this.nameCompany = nameCompany;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.bankName = bankName;
        this.bankAccountNumber = bankAccountNumber;
    }

}
