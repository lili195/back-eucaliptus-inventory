package com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Clase que representa un mensaje que se puede enviar o recibir en el sistema.
 * <p>
 * Este objeto contiene un único campo de texto, el cual almacena el mensaje.
 * </p>
 */

@Data
@AllArgsConstructor
public class Message {

    /**
     * El contenido del mensaje.
     * Este valor es obligatorio y no debe ser nulo.
     */

    @NotNull
    private String message;

    /**
     * Constructor por defecto de la clase Message.
     * Este constructor es necesario para instanciar el objeto sin parámetros.
     */

    public Message(){
    }
}
