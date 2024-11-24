package com.uptc.tc.eucaliptus.securityAPI.infraestructure.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Clase que representa un objeto de transferencia de datos (DTO)
 * para un token de autenticación.
 * <p>
 * Contiene la información asociada al token, como el token en sí, el rol del usuario,
 * el nombre de usuario y el correo electrónico.
 * </p>
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TokenDTO {

    /**
     * Token generado para autenticar al usuario.
     * Este valor no debe ser nulo.
     */

    @NotNull
    private String token;

    /**
     * Rol asignado al usuario autenticado.
     * Este valor no debe ser nulo.
     */

    @NotNull
    private String role;

    /**
     * Nombre de usuario asociado al token.
     * Este valor no debe ser nulo.
     */

    @NotNull
    private String username;

    /**
     * Correo electrónico del usuario asociado al token.
     * Este valor no debe ser nulo.
     */

    @NotNull
    private String email;
}
