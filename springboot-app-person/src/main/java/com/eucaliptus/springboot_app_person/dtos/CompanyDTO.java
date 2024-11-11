package com.eucaliptus.springboot_app_person.dtos;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDTO {
    private String nit;
    private String companyName;
    private String companyPhoneNumber;
    private String companyEmail;
    private String companyAddress;
}
