package com.eucaliptus.springboot_app_person.mappers;

import com.eucaliptus.springboot_app_person.dtos.PersonDTO;
import com.eucaliptus.springboot_app_person.dtos.SellerDTO;
import com.eucaliptus.springboot_app_person.model.*;

public class SellerMapper {

    public static Seller sellerDTOToSeller(SellerDTO sellerDTO, DocumentType documentType) {
        PersonDTO personDTO = sellerDTO.getPersonDTO();
        Seller seller = new Seller (personDTO.getIdPerson(), personDTO.getFirstName(), personDTO.getLastName(), personDTO.getEmail(), personDTO.getAddress(), personDTO.getPhoneNumber(), documentType);
        seller.setUsername(sellerDTO.getUsername());
        return seller;
    }

    public static SellerDTO sellerToSellerDTO(Seller seller) {
        SellerDTO sellerDTO = new SellerDTO();
        PersonDTO personDTO = PersonMapper.personToPersonDTO(seller);
        sellerDTO.setPersonDTO(personDTO);
        sellerDTO.setUsername(seller.getUsername());
        return sellerDTO;
    }
}
