package com.eucaliptus.springboot_app_billing.dto;

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
