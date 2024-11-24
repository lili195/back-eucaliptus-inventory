package com.uptc.tc.eucaliptus.securityAPI.infraestructure.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Clase que representa un objeto de transferencia de datos (DTO)
 * para la actualización de información de un usuario.
 * <p>
 * Contiene los datos necesarios para actualizar el nombre de usuario
 * y el correo electrónico asociado a un usuario existente.
 * </p>
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateUserDTO {

    /**
     * Nombre de usuario actual que se desea actualizar.
     * Este valor no debe ser nulo.
     */

    @NotNull
    private String oldUsername;

    /**
     * Nuevo nombre de usuario que se desea asignar.
     * Este valor no debe ser nulo.
     */

    @NotNull
    private String newUsername;

    /**
     * Nuevo correo electrónico que se desea asociar al usuario.
     * Este valor no debe ser nulo y debe cumplir con el formato de un correo válido.
     */

    @Email
    @NotNull
    private String email;
}
