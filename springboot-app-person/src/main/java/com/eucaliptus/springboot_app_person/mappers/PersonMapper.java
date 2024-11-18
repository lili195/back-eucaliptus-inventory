package com.eucaliptus.springboot_app_person.mappers;

import com.eucaliptus.springboot_app_person.dtos.PersonDTO;
import com.eucaliptus.springboot_app_person.model.Person;

public class PersonMapper {

//    public static Person personDTOToPerson(PersonDTO personDTO, Role role, DocumentType documentType) {
//        Person person = new Person();
//        person.setIdNumber(personDTO.getIdPerson());
//        person.setFirstName(personDTO.getFirstName());
//        person.setLastName(personDTO.getLastName());
//        person.setEmail(personDTO.getEmail());
//        person.setPhoneNumber(personDTO.getPhoneNumber());
//        person.setDocumentType(documentType);
//        person.setRole(role);
//        return person;
//    }

    public static PersonDTO personToPersonDTO(Person person) {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setIdPerson(person.getIdNumber());
        personDTO.setFirstName(person.getFirstName());
        personDTO.setLastName(person.getLastName());
        personDTO.setEmail(person.getEmail());
        personDTO.setAddress(person.getAddress());
        personDTO.setPhoneNumber(person.getPhoneNumber());
        personDTO.setDocumentType(person.getDocumentType().getNameType().name());
        return personDTO;
    }
}
