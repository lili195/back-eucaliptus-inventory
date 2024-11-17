package com.eucaliptus.springboot_app_billing.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {
    private String idClient;
    private String nameClient;
    private String email;
}
