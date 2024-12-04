package com.eucaliptus.springboot_app_person.mappers;

import com.eucaliptus.springboot_app_person.dtos.PersonDTO;
import com.eucaliptus.springboot_app_person.dtos.SellerDTO;
import com.eucaliptus.springboot_app_person.model.*;

/**
 * Clase responsable de mapear objetos de tipo {@link Seller} y {@link SellerDTO}.
 * Esta clase facilita la conversión de datos entre la entidad {@link Seller} y el DTO {@link SellerDTO},
 * permitiendo la transferencia de información entre capas de la aplicación de manera eficiente.
 */

public class SellerMapper {

    /**
     * Convierte un objeto {@link SellerDTO} en un {@link Seller}.
     *
     * @param sellerDTO El objeto {@link SellerDTO} que contiene los datos a mapear.
     * @param documentType El tipo de documento {@link DocumentType} que se asignará al vendedor.
     * @return Un objeto {@link Seller} con los datos mapeados desde el {@link SellerDTO}.
     */

    public static Seller sellerDTOToSeller(SellerDTO sellerDTO, DocumentType documentType) {
        PersonDTO personDTO = sellerDTO.getPersonDTO();
        Seller seller = new Seller (personDTO.getIdPerson(), personDTO.getFirstName(), personDTO.getLastName(), personDTO.getEmail(), personDTO.getAddress(), personDTO.getPhoneNumber(), documentType);
        seller.setUsername(sellerDTO.getUsername());
        return seller;
    }

    /**
     * Convierte un objeto {@link Seller} en un {@link SellerDTO}.
     *
     * @param seller El objeto {@link Seller} que contiene los datos a mapear.
     * @return Un objeto {@link SellerDTO} con los datos mapeados desde el {@link Seller}.
     */

    public static SellerDTO sellerToSellerDTO(Seller seller) {
        SellerDTO sellerDTO = new SellerDTO();
        PersonDTO personDTO = PersonMapper.personToPersonDTO(seller);
        sellerDTO.setPersonDTO(personDTO);
        sellerDTO.setUsername(seller.getUsername());
        return sellerDTO;
    }
}
