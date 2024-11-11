package com.eucaliptus.springboot_app_person.mappers;

import com.eucaliptus.springboot_app_person.dtos.CompanyDTO;
import com.eucaliptus.springboot_app_person.model.Company;
import com.eucaliptus.springboot_app_person.model.DocumentType;

public class CompanyMapper {

    public static Company companyDTOToCompany(CompanyDTO companyDTO, DocumentType documentType) {
        return new Company(companyDTO.getNit(),
                companyDTO.getCompanyName(),
                null,
                companyDTO.getCompanyEmail(),
                companyDTO.getCompanyAddress(),
                companyDTO.getCompanyPhoneNumber(),
                documentType
        );
    }

    public static CompanyDTO companyToCompanyDTO(Company company) {
        return new CompanyDTO(
                company.getIdNumber(),
                company.getFirstName(),
                company.getPhoneNumber(),
                company.getEmail(),
                company.getAddress()
        );
    }
}
