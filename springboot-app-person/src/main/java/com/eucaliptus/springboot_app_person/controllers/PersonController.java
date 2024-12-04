package com.eucaliptus.springboot_app_person.controllers;

import com.eucaliptus.springboot_app_person.dtos.Message;
import com.eucaliptus.springboot_app_person.mappers.PersonMapper;
import com.eucaliptus.springboot_app_person.model.Person;
import com.eucaliptus.springboot_app_person.model.Seller;
import com.eucaliptus.springboot_app_person.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestionar las operaciones relacionadas con las personas.
 * <p>
 * Este controlador proporciona varios endpoints para gestionar las personas, incluidos
 * la obtención de todas las personas, la búsqueda de una persona por su ID y la
 * obtención del administrador.
 * </p>
 */
@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    /**
     * Obtiene todas las personas.
     *
     * @return una lista de todas las personas.
     */
    @GetMapping
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    /**
     * Obtiene una persona por su ID.
     *
     * @param id el ID de la persona a obtener.
     * @return una respuesta con la persona encontrada o un estado "Bad Request" si no se encuentra.
     */
    @GetMapping("/getPersonById/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable String id) {
        return personService.getPersonById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Obtiene el administrador del sistema.
     * Este endpoint está restringido a usuarios con el rol de "ROLE_ADMIN".
     *
     * @return una respuesta con el administrador encontrado, o un mensaje de error si no se encuentra.
     */
    @GetMapping("/getAdmin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> getAdmin(){
        try {
            Optional<Seller> opPerson = personService.getAdmin();
            if (opPerson.isPresent())
                return new ResponseEntity<>(PersonMapper.personToPersonDTO(opPerson.get()), HttpStatus.OK);
            return new ResponseEntity<>(new Message("Admin no encontrado"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
