package com.eucaliptus.springboot_app_billing.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ProviderDTO {
    private PersonDTO personDTO;
    private String bankName;
    private String bankAccountNumber;
    private String personType;
    private CompanyDTO companyDTO;
}
