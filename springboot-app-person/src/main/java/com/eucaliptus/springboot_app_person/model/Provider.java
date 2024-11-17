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
public class Provider extends Person{

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_account_number")
    private String bankAccountNumber;

    @Column(name = "person_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumPersonType personType;

    @ManyToOne(optional = true)
    @JoinColumn(name = "nit_company", referencedColumnName = "nit_company")
    private Company company;

    public Provider (String idNumber, String firstName, String lastName, String email, String address, String phoneNumber, DocumentType documentType,
                     String bankName, String bankAccountNumber, EnumPersonType personType, Company company){
        super(idNumber, firstName, lastName, email, address, phoneNumber, documentType, EnumRole.ROLE_PROVIDER);
        this.bankName = bankName;
        this.bankAccountNumber = bankAccountNumber;
        this.personType = personType;
        this.company = company;
    }

    public Provider (String idNumber, String firstName, String lastName, String email, String address, String phoneNumber, DocumentType documentType){
        super(idNumber, firstName, lastName, email, address, phoneNumber, documentType, EnumRole.ROLE_PROVIDER);
    }

}
