package com.eucaliptus.springboot_app_person.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SellerDTO {
    private String idSeller;
    private PersonDTO personDTO;
    private String documentType;
    private String documentNumber;
    private String username;
    private String password;
    private String homeAddress;
}
