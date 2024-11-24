package com.uptc.tc.eucaliptus.securityAPI.infraestructure.dtos;

import lombok.*;

/**
 * Clase que representa un objeto de transferencia de datos (DTO)
 * para la recuperación de contraseña.
 * <p>
 * Contiene información necesaria para realizar el cambio de contraseña,
 * como el correo electrónico, la nueva contraseña y el código de recuperación.
 * </p>
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RecoveryPasswordDTO {
    /**
     * Dirección de correo electrónico asociada a la cuenta cuyo propietario
     * desea recuperar la contraseña.
     */

    private String email;

    /**
     * Nueva contraseña que se establecerá para la cuenta.
     */

    private String newPassword;

    /**
     * Código de recuperación enviado al usuario para verificar su identidad.
     */

    private int code;
}
