package com.eucaliptus.springboot_app_person.dtos;

import jakarta.annotation.Nonnull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateUserDTO {
    private String firstName;
    private String lastName;
    private String oldUsername;
    private String newUsername;
    private String email;
}
