package com.eucaliptus.springboot_app_person.mappers;

import com.eucaliptus.springboot_app_person.dtos.PersonDTO;
import com.eucaliptus.springboot_app_person.model.Person;

/**
 * Clase responsable de la conversión entre objetos de tipo {@link Person} y {@link PersonDTO}.
 * Esta clase facilita el mapeo de datos entre la entidad {@link Person} y el DTO {@link PersonDTO},
 * asegurando que la información pueda ser transferida entre las capas de la aplicación de manera eficiente.
 */

public class PersonMapper {

    /**
     * Convierte un objeto {@link Person} en un {@link PersonDTO}.
     *
     * @param person El objeto {@link Person} que contiene los datos a mapear.
     * @return Un objeto {@link PersonDTO} con los datos mapeados desde el {@link Person}.
     */

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
