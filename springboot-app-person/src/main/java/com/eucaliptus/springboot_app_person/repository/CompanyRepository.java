package com.eucaliptus.springboot_app_person.repository;

import com.eucaliptus.springboot_app_person.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para acceder y manejar entidades de tipo {@link Company} en la base de datos.
 *
 * La interfaz {@link CompanyRepository} extiende {@link JpaRepository}, lo que permite realizar
 * operaciones CRUD (crear, leer, actualizar y eliminar) sobre las entidades {@link Company}.
 * Además, incluye métodos específicos para verificar la existencia de compañías en la base de datos
 * basándose en su número de identificación (NIT).
 */

@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {

    /**
     * Verifica si ya existe una compañía con el NIT especificado.
     *
     * @param nitCompany El NIT de la compañía que se desea verificar.
     * @return {@code true} si existe una compañía con el NIT proporcionado;
     *         {@code false} si no existe.
     */

    boolean existsByNitCompany(String nitCompany);

}
