package com.uptc.tc.eucaliptus.securityAPI.infraestructure.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RecoveryCodeDTO {
    private String email;
    private String username;
    private int code;
}
