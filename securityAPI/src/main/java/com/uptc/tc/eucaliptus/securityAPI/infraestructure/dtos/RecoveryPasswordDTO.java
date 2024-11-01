package com.uptc.tc.eucaliptus.securityAPI.infraestructure.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RecoveryPasswordDTO {
    private String email;
    private String newPassword;
    private int code;
}
