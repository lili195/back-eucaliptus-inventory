package com.eucaliptus.springboot_app_person.mappers;

import com.eucaliptus.springboot_app_person.dtos.CompanyDTO;
import com.eucaliptus.springboot_app_person.model.Company;

public class CompanyMapper {

    public static Company companyDTOToCompany(CompanyDTO companyDTO) {
        return new Company(companyDTO.getNit(),
                companyDTO.getCompanyName(),
                companyDTO.getCompanyEmail(),
                companyDTO.getCompanyPhoneNumber(),
                companyDTO.getCompanyAddress()
        );
    }

    public static CompanyDTO companyToCompanyDTO(Company company) {
        return new CompanyDTO(
                company.getNitCompany(),
                company.getNameCompany(),
                company.getEmailCompany(),
                company.getPhoneNumberCompany(),
                company.getAddressCompany()
        );
    }
}
