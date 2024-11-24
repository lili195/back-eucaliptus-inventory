package com.uptc.tc.eucaliptus.securityAPI.infraestructure.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Clase que representa los datos de inicio de sesión de un usuario.
 * <p>
 * Incluye las credenciales necesarias: nombre de usuario y contraseña.
 * Utiliza anotaciones de validación para garantizar que ambos campos no estén en blanco.
 * </p>
 */

@Data
@Getter
@Setter
public class LoginUser {

    /**
     * Nombre de usuario del usuario que intenta iniciar sesión.
     * <p>
     * Este campo no puede estar en blanco.
     * </p>
     */

    @NotBlank
    private String username;

    /**
     * Contraseña del usuario que intenta iniciar sesión.
     * <p>
     * Este campo no puede estar en blanco.
     * </p>
     */

    @NotBlank
    private String password;

}