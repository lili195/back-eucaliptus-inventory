package com.eucaliptus.springboot_app_person.mappers;

import com.eucaliptus.springboot_app_person.dtos.PersonDTO;
import com.eucaliptus.springboot_app_person.model.Person;
import com.eucaliptus.springboot_app_person.model.Role;

public class PersonMapper {

    public static Person personDTOToPerson(PersonDTO personDTO, Role role) {
        Person person = new Person();
        person.setIdNumber(personDTO.getIdPerson());
        person.setFirstName(personDTO.getFirstName());
        person.setLastName(personDTO.getLastName());
        person.setEmail(personDTO.getEmail());
        person.setPhoneNumber(personDTO.getPhoneNumber());
        person.setRole(role);
        return person;
    }

    public static PersonDTO personToPersonDTO(Person person) {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setIdPerson(person.getIdNumber());
        personDTO.setFirstName(person.getFirstName());
        personDTO.setLastName(person.getLastName());
        personDTO.setEmail(person.getEmail());
        personDTO.setPhoneNumber(person.getPhoneNumber());
        personDTO.setRole(person.getRole().getNameRole().name());
        return personDTO;
    }
}