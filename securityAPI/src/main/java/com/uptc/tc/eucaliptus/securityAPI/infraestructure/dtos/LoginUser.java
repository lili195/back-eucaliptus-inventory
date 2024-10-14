package com.uptc.tc.eucaliptus.securityAPI.infraestructure.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LoginUser {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

}