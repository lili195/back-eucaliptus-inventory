package com.uptc.tc.eucaliptus.securityAPI.infraestructure.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    @NotNull
    private String username;
    @Email
    @NotNull
    private String email;
    @NotNull
    private String password;
    private String role;
}
