package com.eucaliptus.springboot_app_person.dtos;

import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

/**
 * Data Transfer Object (DTO) para representar la información básica de un usuario.
 * Esta clase encapsula los datos necesarios para crear o interactuar con un usuario
 * en el sistema, como nombre de usuario, correo electrónico, contraseña y rol.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    /**
     * El nombre de usuario único del usuario.
     */

    private String username;

    /**
     * El correo electrónico asociado al usuario.
     */

    private String email;

    /**
     * La contraseña del usuario.
     */

    private String password;

    /**
     * El rol asignado al usuario en el sistema, como "ADMIN" o "USER".
     */

    private String role;
}
