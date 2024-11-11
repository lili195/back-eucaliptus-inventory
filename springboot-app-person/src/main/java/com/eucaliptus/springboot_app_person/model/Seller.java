package com.eucaliptus.springboot_app_person.model;


import com.eucaliptus.springboot_app_person.enums.EnumRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "sellers")
//@DiscriminatorValue("ROLE_SELLER")
public class Seller extends Person{

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    public Seller(String idNumber, String firstName, String lastName, String email, String address, String phoneNumber, DocumentType documentType,
                  String username) {
        super(idNumber, firstName, lastName, email, address, phoneNumber, documentType, EnumRole.ROLE_SELLER);
        this.username = username;
    }

    public Seller(String idNumber, String firstName, String lastName, String email, String address, String phoneNumber, DocumentType documentType) {
        super(idNumber, firstName, lastName, email, address, phoneNumber, documentType, EnumRole.ROLE_SELLER);
    }
}
