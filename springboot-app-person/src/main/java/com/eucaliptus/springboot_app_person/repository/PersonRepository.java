package com.eucaliptus.springboot_app_person.repository;

import com.eucaliptus.springboot_app_person.enums.EnumRole;
import com.eucaliptus.springboot_app_person.model.Person;
import com.eucaliptus.springboot_app_person.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para acceder y manejar entidades de tipo {@link Person} en la base de datos.
 *
 * La interfaz {@link PersonRepository} extiende {@link JpaRepository}, proporcionando operaciones CRUD
 * sobre las entidades {@link Person}. Además, incluye métodos específicos para buscar personas por su
 * número de identificación, verificar si existen, y encontrar personas activas o de un rol específico.
 */

@Repository
public interface PersonRepository extends JpaRepository<Person, String> {

    /**
     * Verifica si existe una persona con el número de identificación proporcionado.
     *
     * @param idNumber El número de identificación de la persona que se desea verificar.
     * @return {@code true} si existe una persona con el número de identificación proporcionado;
     *         {@code false} si no existe.
     */

    boolean existsByIdNumber(String idNumber);

    /**
     * Encuentra una persona por su número de identificación.
     *
     * @param idNumber El número de identificación de la persona que se desea buscar.
     * @return Un {@link Optional} que contiene la persona encontrada,
     *         o {@link Optional#empty()} si no se encuentra ninguna persona con ese número de identificación.
     */
    Optional<Person> findByIdNumber(String idNumber);

    /**
     * Encuentra una persona activa por su número de identificación.
     *
     * @param idNumber El número de identificación de la persona activa que se desea buscar.
     * @return Un {@link Optional} que contiene la persona activa encontrada,
     *         o {@link Optional#empty()} si no se encuentra ninguna persona activa con ese número de identificación.
     */
    Optional<Person> findByActiveTrueAndIdNumber(String idNumber);

    /**
     * Encuentra un {@link Seller} por su rol {@link EnumRole}.
     *
     * @param role El rol del vendedor que se desea buscar.
     * @return Un {@link Optional} que contiene el {@link Seller} encontrado,
     *         o {@link Optional#empty()} si no se encuentra ningún vendedor con el rol proporcionado.
     */
    Optional<Seller> findByRole(EnumRole role);
}