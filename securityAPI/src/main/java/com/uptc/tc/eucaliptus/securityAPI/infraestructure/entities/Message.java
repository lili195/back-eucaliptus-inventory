package com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
public class Message {
    @NotNull
    private String message;

    public Message(){
    }
}
