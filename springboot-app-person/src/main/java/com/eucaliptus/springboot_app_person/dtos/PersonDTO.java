package com.eucaliptus.springboot_app_person.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PersonDTO {
    private String idPerson;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phoneNumber;
    private String documentType;
}
