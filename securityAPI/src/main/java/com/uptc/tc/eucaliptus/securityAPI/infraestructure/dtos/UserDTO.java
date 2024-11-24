package com.uptc.tc.eucaliptus.securityAPI.infraestructure.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Clase que representa un objeto de transferencia de datos (DTO) para usuarios.
 * <p>
 * Contiene información básica necesaria para crear o gestionar usuarios en el sistema.
 * </p>
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    /**
     * Nombre de usuario único que identifica al usuario.
     * Este valor es obligatorio y no debe ser nulo.
     */

    @NotNull
    private String username;

    /**
     * Nombre de usuario único que identifica al usuario.
     * Este valor es obligatorio y no debe ser nulo.
     */

    @Email
    @NotNull
    private String email;

    /**
     * Contraseña asociada al usuario.
     * Este valor es obligatorio y no debe ser nulo.
     */

    @NotNull
    private String password;

    /**
     * Rol asignado al usuario, que determina los permisos o nivel de acceso.
     * Este valor es opcional.
     */

    private String role;
}
