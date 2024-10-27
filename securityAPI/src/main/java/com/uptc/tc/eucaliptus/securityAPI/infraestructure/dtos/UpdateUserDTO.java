package com.uptc.tc.eucaliptus.securityAPI.infraestructure.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateUserDTO {
    @NotNull
    private String oldUsername;
    @NotNull
    private String newUsername;
    @Email
    @NotNull
    private String email;
}
