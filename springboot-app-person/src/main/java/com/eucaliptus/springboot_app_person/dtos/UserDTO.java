package com.eucaliptus.springboot_app_person.dtos;

import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    private String username;
    private String email;
    private String password;
    private String role;
}
