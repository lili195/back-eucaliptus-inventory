package com.eucaliptus.springboot_app_person.services;

import com.eucaliptus.springboot_app_person.enums.EnumRole;
import com.eucaliptus.springboot_app_person.model.Person;
import com.eucaliptus.springboot_app_person.model.Seller;
import com.eucaliptus.springboot_app_person.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * Servicio encargado de gestionar las operaciones relacionadas con las personas.
 * Esta clase proporciona métodos para:
 * - Obtener todas las personas.
 * - Buscar una persona por su número de identificación.
 * - Buscar una persona activa por su número de identificación.
 * - Obtener un administrador del sistema.
 * - Guardar una nueva persona.
 * - Actualizar los detalles de una persona existente.
 * - Verificar la existencia de una persona por su ID.
 * - Eliminar (desactivar) una persona.
 */

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    /**
     * Obtiene todas las personas almacenadas en la base de datos.
     *
     * Este método devuelve una lista con todas las personas registradas en el repositorio.
     *
     * @return Una lista de todas las personas.
     */

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    /**
     * Busca una persona por su número de identificación.
     *
     * Este método consulta la base de datos buscando una persona cuyo número de identificación coincida con el proporcionado.
     *
     * @param idNumber El número de identificación de la persona a buscar.
     * @return Un {@code Optional} que contiene la persona encontrada, o {@code Optional.empty()} si no se encuentra ninguna con ese número de identificación.
     */
    public Optional<Person> getPersonById(String idNumber) {
        return personRepository.findByIdNumber(idNumber);
    }

    /**
     * Busca una persona activa por su número de identificación.
     *
     * Este método consulta la base de datos buscando una persona cuyo número de identificación coincida con el proporcionado y que esté activa.
     *
     * @param idNumber El número de identificación de la persona activa a buscar.
     * @return Un {@code Optional} que contiene la persona activa encontrada, o {@code Optional.empty()} si no se encuentra ninguna persona activa con ese número de identificación.
     */
    public Optional<Person> getActivePersonById(String idNumber) {
        return personRepository.findByActiveTrueAndIdNumber(idNumber);
    }

    /**
     * Obtiene el administrador del sistema.
     *
     * Este método busca una persona con el rol de administrador en la base de datos.
     *
     * @return Un {@code Optional} que contiene la persona con el rol de administrador, o {@code Optional.empty()} si no se encuentra ninguna persona con ese rol.
     */
    public Optional<Seller> getAdmin(){
        return personRepository.findByRole(EnumRole.ROLE_ADMIN);
    }

    /**
     * Guarda una nueva persona en la base de datos.
     *
     * Este método guarda una nueva instancia de la clase {@code Person} en el repositorio.
     *
     * @param person La persona que se desea guardar.
     * @return La persona guardada, incluyendo el ID asignado si es la primera vez que se guarda.
     */
    public Person savePerson(Person person) {
        return personRepository.save(person);
    }

    /**
     * Actualiza los detalles de una persona existente.
     *
     * Este método busca una persona con el número de identificación proporcionado y, si se encuentra, actualiza sus detalles con los valores proporcionados en {@code personDetails}.
     * Luego guarda los detalles actualizados en el repositorio.
     *
     * @param idNumber El número de identificación de la persona a actualizar.
     * @param personDetails Un objeto {@code Person} que contiene los nuevos detalles de la persona.
     * @return Un {@code Optional} que contiene la persona actualizada, o {@code Optional.empty()} si no se encuentra una persona con ese número de identificación.
     */
    public Optional<Person> updatePerson(String idNumber, Person personDetails) {
        return personRepository.findByIdNumber(idNumber).map(person -> {
            person.setFirstName(personDetails.getFirstName());
            person.setLastName(personDetails.getLastName());
            person.setPhoneNumber(personDetails.getPhoneNumber());
            person.setEmail(personDetails.getEmail());
            person.setDocumentType(personDetails.getDocumentType());
            return personRepository.save(person);
        });
    }

    /**
     * Verifica si existe una persona con el número de identificación proporcionado.
     *
     * Este método consulta el repositorio para verificar si existe una persona con el número de identificación dado.
     *
     * @param idPerson El número de identificación de la persona a verificar.
     * @return {@code true} si la persona existe, {@code false} si no.
     */
    public boolean existsByIdPerson(String idPerson) {
        return personRepository.existsById(idPerson);
    }

    /**
     * Elimina (desactiva) una persona mediante la actualización de su estado de actividad.
     *
     * Este método busca una persona con el número de identificación proporcionado y, si se encuentra, la desactiva (establece su campo {@code active} en {@code false}).
     *
     * @param idNumber El número de identificación de la persona a desactivar.
     * @return {@code true} si la persona fue desactivada exitosamente, o {@code false} si no se encuentra ninguna persona con ese número de identificación.
     */
    public boolean deletePerson(String idNumber) {
        return personRepository.findByIdNumber(idNumber).map(person -> {
            person.setActive(false);
            personRepository.save(person);
            return true;
        }).orElse(false);
    }
}
