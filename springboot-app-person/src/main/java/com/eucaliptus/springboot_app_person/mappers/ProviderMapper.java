package com.eucaliptus.springboot_app_person.mappers;

import com.eucaliptus.springboot_app_person.dtos.CompanyDTO;
import com.eucaliptus.springboot_app_person.dtos.PersonDTO;
import com.eucaliptus.springboot_app_person.dtos.ProviderDTO;
import com.eucaliptus.springboot_app_person.enums.EnumPersonType;
import com.eucaliptus.springboot_app_person.model.*;

/**
 * Clase responsable de la conversión entre objetos de tipo {@link Provider} y {@link ProviderDTO}.
 * Esta clase facilita el mapeo de datos entre la entidad {@link Provider} y el DTO {@link ProviderDTO},
 * asegurando que la información pueda ser transferida entre las capas de la aplicación de manera eficiente.
 */

public class ProviderMapper {

    /**
     * Convierte un objeto {@link ProviderDTO} en un {@link Provider}.
     *
     * @param providerDTO El objeto {@link ProviderDTO} que contiene los datos a mapear.
     * @param documentType El tipo de documento {@link DocumentType} que se asignará al proveedor.
     * @return Un objeto {@link Provider} con los datos mapeados desde el {@link ProviderDTO}.
     */

    public static Provider providerDTOToProvider(ProviderDTO providerDTO, DocumentType documentType) {
        PersonDTO personDTO = providerDTO.getPersonDTO();
        Provider provider = new Provider (personDTO.getIdPerson(), personDTO.getFirstName(), personDTO.getLastName(), personDTO.getEmail(), personDTO.getAddress(), personDTO.getPhoneNumber(), documentType);
        provider.setBankName(providerDTO.getBankName());
        provider.setBankAccountNumber(providerDTO.getBankAccountNumber());
        provider.setPersonType(EnumPersonType.valueOf(providerDTO.getPersonType()));
        return provider;
    }

    /**
     * Convierte un objeto {@link Provider} en un {@link ProviderDTO}.
     *
     * @param provider El objeto {@link Provider} que contiene los datos a mapear.
     * @return Un objeto {@link ProviderDTO} con los datos mapeados desde el {@link Provider}.
     */

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
