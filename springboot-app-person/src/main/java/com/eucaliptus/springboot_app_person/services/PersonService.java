package com.eucaliptus.springboot_app_person.services;

import com.eucaliptus.springboot_app_person.model.Person;
import com.eucaliptus.springboot_app_person.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Optional<Person> getPersonById(String idNumber) {
        return personRepository.findByIdNumber(idNumber);
    }

    public Person savePerson(Person person) {
        return personRepository.save(person);
    }

    public Optional<Person> updatePerson(String idNumber, Person personDetails) {
        return personRepository.findByIdNumber(idNumber).map(person -> {
            person.setFirstName(personDetails.getFirstName());
            person.setLastName(personDetails.getLastName());
            person.setPhoneNumber(personDetails.getPhoneNumber());
            person.setEmail(personDetails.getEmail());
            person.setRole(personDetails.getRole());
            return personRepository.save(person);
        });
    }

    public boolean existsByIdPerson(String idPerson) {
        return personRepository.existsById(idPerson);
    }

    public boolean deletePerson(String idNumber) {
        return personRepository.findByIdNumber(idNumber).map(person -> {
            personRepository.delete(person);
            return true;
        }).orElse(false);
    }
}
