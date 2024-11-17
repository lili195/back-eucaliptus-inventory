package com.eucaliptus.springboot_app_person.mappers;

import com.eucaliptus.springboot_app_person.dtos.CompanyDTO;
import com.eucaliptus.springboot_app_person.dtos.PersonDTO;
import com.eucaliptus.springboot_app_person.dtos.ProviderDTO;
import com.eucaliptus.springboot_app_person.enums.EnumPersonType;
import com.eucaliptus.springboot_app_person.model.*;

public class ProviderMapper {

    public static Provider providerDTOToProvider(ProviderDTO providerDTO, DocumentType documentType) {
        PersonDTO personDTO = providerDTO.getPersonDTO();
        Provider provider = new Provider (personDTO.getIdPerson(), personDTO.getFirstName(), personDTO.getLastName(), personDTO.getEmail(), personDTO.getAddress(), personDTO.getPhoneNumber(), documentType);
        provider.setBankName(providerDTO.getBankName());
        provider.setBankAccountNumber(providerDTO.getBankAccountNumber());
        provider.setPersonType(EnumPersonType.valueOf(providerDTO.getPersonType()));
        return provider;
    }

    public static ProviderDTO providerToProviderDTO(Provider provider) {
        ProviderDTO providerDTO = new ProviderDTO();
        PersonDTO personDTO = PersonMapper.personToPersonDTO(provider);
        providerDTO.setPersonDTO(personDTO);
        providerDTO.setBankName(provider.getBankName());
        providerDTO.setBankAccountNumber(provider.getBankAccountNumber());
        providerDTO.setPersonType(provider.getPersonType().name());
        if (provider.getCompany() != null) {
            CompanyDTO companyDTO = CompanyMapper.companyToCompanyDTO(provider.getCompany());
            providerDTO.setCompanyDTO(companyDTO);
        }
        return providerDTO;
    }
}
