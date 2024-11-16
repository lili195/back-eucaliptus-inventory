package com.eucaliptus.springboot_app_person.model;

import com.eucaliptus.springboot_app_person.enums.EnumPersonType;
import com.eucaliptus.springboot_app_person.enums.EnumRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Setter
@Getter
@Table(name = "providers")
//@DiscriminatorValue("ROLE_PROVIDER")
public class Provider extends Person{

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_account_number")
    private String bankAccountNumber;

    @Column(name = "person_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumPersonType personType;

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

    public Provider (String idNumber, String firstName, String lastName, String email, String address, String phoneNumber, DocumentType documentType,
                     String bankName, String bankAccountNumber, EnumPersonType personType,
                     String nitCompany, String nameCompany, String emailCompany, String phoneNumberCompany, String addressCompany){
        super(idNumber, firstName, lastName, email, address, phoneNumber, documentType, EnumRole.ROLE_PROVIDER);
        this.bankName = bankName;
        this.bankAccountNumber = bankAccountNumber;
        this.personType = personType;
        this.nitCompany = nitCompany;
        this.nameCompany = nameCompany;
        this.emailCompany = emailCompany;
        this.phoneNumberCompany = phoneNumberCompany;
        this.addressCompany = addressCompany;
    }

    public Provider (String idNumber, String firstName, String lastName, String email, String address, String phoneNumber, DocumentType documentType){
        super(idNumber, firstName, lastName, email, address, phoneNumber, documentType, EnumRole.ROLE_PROVIDER);
    }

}
