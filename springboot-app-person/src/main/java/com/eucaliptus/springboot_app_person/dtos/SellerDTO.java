package com.eucaliptus.springboot_app_person.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SellerDTO {
    private PersonDTO personDTO;
    private String username;
    private String password;
}
