package com.uptc.tc.eucaliptus.securityAPI.infraestructure.dtos;

import lombok.*;

/**
 * Clase que representa un objeto de transferencia de datos (DTO)
 * para la recuperación de cuentas mediante un código.
 * <p>
 * Contiene información necesaria para realizar la recuperación de la cuenta,
 * como el correo electrónico, el nombre de usuario y el código de recuperación.
 * </p>
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RecoveryCodeDTO {

    /**
     * Dirección de correo electrónico asociada a la cuenta que se intenta recuperar.
     */

    private String email;

    /**
     * Nombre de usuario asociado a la cuenta que se intenta recuperar.
     */

    private String username;

    /**
     * Código de recuperación enviado al usuario para verificar su identidad.
     */

    private int code;
}
