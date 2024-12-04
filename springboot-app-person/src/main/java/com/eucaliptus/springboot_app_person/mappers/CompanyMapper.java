package com.eucaliptus.springboot_app_person.mappers;

import com.eucaliptus.springboot_app_person.dtos.CompanyDTO;
import com.eucaliptus.springboot_app_person.model.Company;

/**
 * Clase responsable de la conversión entre objetos de tipo {@link Company} y {@link CompanyDTO}.
 * Esta clase contiene métodos estáticos para mapear datos entre los modelos de entidad y los DTOs (Data Transfer Objects),
 * facilitando la transferencia de información entre capas de la aplicación.
 */
public class CompanyMapper {

    /**
     * Convierte un {@link CompanyDTO} en un objeto {@link Company}.
     *
     * @param companyDTO El objeto {@link CompanyDTO} que contiene los datos a mapear.
     * @return Un objeto {@link Company} con los datos mapeados desde el {@link CompanyDTO}.
     */

    public static Company companyDTOToCompany(CompanyDTO companyDTO) {
        return new Company(companyDTO.getNit(),
                companyDTO.getCompanyName(),
                companyDTO.getCompanyEmail(),
                companyDTO.getCompanyPhoneNumber(),
                companyDTO.getCompanyAddress()
        );
    }

    /**
     * Convierte un objeto {@link Company} en un {@link CompanyDTO}.
     *
     * @param company El objeto {@link Company} que contiene los datos a mapear.
     * @return Un objeto {@link CompanyDTO} con los datos mapeados desde el {@link Company}.
     */

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
