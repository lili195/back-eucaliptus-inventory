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
//@DiscriminatorValue("ROLE_COMPANY")
public class Company extends Person{

    public Company(String idNumber, String firstName, String lastName, String email, String address, String phoneNumber, DocumentType documentType){
        super(idNumber, firstName, lastName, email, address, phoneNumber, documentType, EnumRole.ROLE_COMPANY);
    }

}
