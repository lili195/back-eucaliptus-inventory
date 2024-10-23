package com.eucaliptus.springboot_app_person.mappers;

import com.eucaliptus.springboot_app_person.dtos.CompanyDTO;
import com.eucaliptus.springboot_app_person.dtos.PersonDTO;
import com.eucaliptus.springboot_app_person.dtos.ProviderDTO;
import com.eucaliptus.springboot_app_person.enums.EnumPersonType;
import com.eucaliptus.springboot_app_person.model.*;

public class ProviderMapper {

    public static Provider providerDTOToProvider(ProviderDTO providerDTO, Role role, DocumentType documentType) {
        Person person = PersonMapper.personDTOToPerson(providerDTO.getPersonDTO(), role, documentType);
        Provider provider = new Provider();
        if (providerDTO.getCompanyDTO() != null) {
            Company company = CompanyMapper.companyDTOToCompany(providerDTO.getCompanyDTO());
            provider.setCompany(company);
        }
        provider.setPerson(person);
        provider.setPersonType(EnumPersonType.valueOf(providerDTO.getPersonType()));
        return provider;
    }

    public static ProviderDTO providerToProviderDTO(Provider provider) {
        ProviderDTO providerDTO = new ProviderDTO();
        providerDTO.setIdProvider(provider.getIdProvider()+"");
        PersonDTO personDTO = PersonMapper.personToPersonDTO(provider.getPerson());
        providerDTO.setPersonDTO(personDTO);
        providerDTO.setPersonType(provider.getPersonType().name());
        if (provider.getCompany() != null) {
            CompanyDTO companyDTO = CompanyMapper.companyToCompanyDTO(provider.getCompany());
            providerDTO.setCompanyDTO(companyDTO);
        }
        return providerDTO;
    }
}
