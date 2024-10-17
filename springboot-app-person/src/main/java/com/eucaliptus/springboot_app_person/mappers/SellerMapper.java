package com.eucaliptus.springboot_app_person.mappers;

import com.eucaliptus.springboot_app_person.dtos.PersonDTO;
import com.eucaliptus.springboot_app_person.dtos.SellerDTO;
import com.eucaliptus.springboot_app_person.model.DocumentType;
import com.eucaliptus.springboot_app_person.model.Person;
import com.eucaliptus.springboot_app_person.model.Role;
import com.eucaliptus.springboot_app_person.model.Seller;

public class SellerMapper {

    public static Seller sellerDTOToSeller(SellerDTO sellerDTO, Role role, DocumentType documentType) {
        Person person = PersonMapper.personDTOToPerson(sellerDTO.getPersonDTO(), role);
        return new Seller(person,
                documentType,
                person.getIdNumber(),
                sellerDTO.getUsername(),
                sellerDTO.getHomeAddress());
    }

    public static SellerDTO sellerToSellerDTO(Seller seller) {
        SellerDTO sellerDTO = new SellerDTO();
        sellerDTO.setIdSeller(seller.getIdSeller()+"");
        PersonDTO personDTO = PersonMapper.personToPersonDTO(seller.getPerson());
        sellerDTO.setPersonDTO(personDTO);
        sellerDTO.setDocumentType(seller.getDocumentType().getNameType().name());
        sellerDTO.setUsername(seller.getUsername());
        sellerDTO.setHomeAddress(seller.getHomeAddress());
        return sellerDTO;
    }
}
