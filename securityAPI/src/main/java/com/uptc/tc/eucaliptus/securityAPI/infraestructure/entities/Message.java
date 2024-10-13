package com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Message {

    private String infoMessage;
}
