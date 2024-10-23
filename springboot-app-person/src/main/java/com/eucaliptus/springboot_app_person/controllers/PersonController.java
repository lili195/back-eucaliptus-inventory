package com.eucaliptus.springboot_app_person.controllers;

import com.eucaliptus.springboot_app_person.dtos.PersonDTO;
import com.eucaliptus.springboot_app_person.enums.EnumDocumentType;
import com.eucaliptus.springboot_app_person.enums.EnumRole;
import com.eucaliptus.springboot_app_person.mappers.PersonMapper;
import com.eucaliptus.springboot_app_person.model.Person;
import com.eucaliptus.springboot_app_person.services.DocumentTypeService;
import com.eucaliptus.springboot_app_person.services.PersonService;
import com.eucaliptus.springboot_app_person.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    @Autowired
    private PersonService personService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DocumentTypeService documentTypeService;

    @GetMapping
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable String id) {
        return personService.getPersonById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Person createPerson(@RequestBody PersonDTO personDTO) {
        Person person = PersonMapper.personDTOToPerson(personDTO,
                roleService.getRoleByName(EnumRole.valueOf(personDTO.getRole())).get(),
                documentTypeService.findByNameType(EnumDocumentType.valueOf(personDTO.getDocumentType())).get());
        return personService.savePerson(person);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable String id, @RequestBody Person personDetails) {
        return personService.updatePerson(id, personDetails)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable String id) {
        return personService.deletePerson(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
