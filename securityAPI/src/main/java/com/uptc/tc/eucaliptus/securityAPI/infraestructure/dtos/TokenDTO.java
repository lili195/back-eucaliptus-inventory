package com.uptc.tc.eucaliptus.securityAPI.infraestructure.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TokenDTO {
    @NotNull
    private String token;
    @NotNull
    private String role;
    @NotNull
    private String username;
}
