package com.eucaliptus.springboot_app_person.dtos;

import jakarta.annotation.Nonnull;
import lombok.*;

/**
 * Data Transfer Object (DTO) para actualizar la información de un usuario.
 * Proporciona los datos necesarios para realizar la operación de actualización,
 * incluyendo cambios en el nombre, nombre de usuario y correo electrónico.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateUserDTO {

    /**
     * El primer nombre del usuario.
     */

    private String firstName;

    /**
     * El apellido del usuario.
     */

    private String lastName;

    /**
     * El nombre de usuario actual del usuario que será actualizado.
     */

    private String oldUsername;

    /**
     * El nuevo nombre de usuario que se desea asignar al usuario.
     */

    private String newUsername;

    /**
     * El correo electrónico del usuario.
     */

    private String email;
}
