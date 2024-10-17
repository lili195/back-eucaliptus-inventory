package com.eucaliptus.springboot_app_person.mappers;

import com.eucaliptus.springboot_app_person.dtos.CompanyDTO;
import com.eucaliptus.springboot_app_person.dtos.PersonDTO;
import com.eucaliptus.springboot_app_person.dtos.ProviderDTO;
import com.eucaliptus.springboot_app_person.enums.EnumPersonType;
import com.eucaliptus.springboot_app_person.model.Company;
import com.eucaliptus.springboot_app_person.model.Person;
import com.eucaliptus.springboot_app_person.model.Provider;
import com.eucaliptus.springboot_app_person.model.Role;

public class ProviderMapper {

    public static Provider providerDTOToProvider(ProviderDTO providerDTO, Role role) {
        Person person = PersonMapper.personDTOToPerson(providerDTO.getPersonDTO(), role);
        Provider provider = new Provider();
        Company company = CompanyMapper.companyDTOToCompany(providerDTO.getCompanyDTO());
        provider.setPerson(person);
        provider.setPersonType(EnumPersonType.valueOf(providerDTO.getPersonType()));
        provider.setCompany(company);
        return provider;
    }

    public static ProviderDTO providerToProviderDTO(Provider provider) {
        ProviderDTO providerDTO = new ProviderDTO();
        providerDTO.setIdProvider(provider.getIdProvider()+"");
        PersonDTO personDTO = PersonMapper.personToPersonDTO(provider.getPerson());
        providerDTO.setPersonDTO(personDTO);
        providerDTO.setPersonType(provider.getPersonType().name());
        CompanyDTO companyDTO =CompanyMapper.companyToCompanyDTO(provider.getCompany());
        providerDTO.setCompanyDTO(companyDTO);
        return providerDTO;
    }
}
