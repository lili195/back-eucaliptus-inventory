package com.eucaliptus.springboot_app_person.repository;

import com.eucaliptus.springboot_app_person.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para acceder y manejar entidades de tipo {@link Provider} en la base de datos.
 *
 * La interfaz {@link ProviderRepository} extiende {@link JpaRepository}, proporcionando operaciones CRUD
 * sobre las entidades {@link Provider}. Además, incluye métodos específicos para buscar proveedores por su
 * número de identificación, verificar si existen, obtener proveedores activos y encontrar proveedores de
 * una compañía específica.
 */

@Repository
public interface ProviderRepository extends JpaRepository<Provider, String> {

    /**
     * Verifica si existe un proveedor con el número de identificación proporcionado.
     *
     * @param id El número de identificación del proveedor que se desea verificar.
     * @return {@code true} si existe un proveedor con el número de identificación proporcionado;
     *         {@code false} si no existe.
     */
    boolean existsByIdNumber(String id);

    /**
     * Verifica si existe un proveedor con el número de identificación proporcionado.
     *
     * @return {@code true} si existe un proveedor con el número de identificación proporcionado;
     *         {@code false} si no existe.
     */
    List<Provider> findByActiveTrue();

    /**
     * Encuentra un proveedor por su número de identificación.
     *
     * @param personId El número de identificación del proveedor que se desea buscar.
     * @return Un {@link Optional} que contiene el proveedor encontrado,
     *         o {@link Optional#empty()} si no se encuentra ningún proveedor con ese número de identificación.
     */
    Optional<Provider> findByIdNumber(String personId);

    /**
     * Encuentra un proveedor activo de una compañía específica por el NIT de la compañía.
     *
     * @param companyId El NIT de la compañía para la que se desea buscar un proveedor activo.
     * @return Un {@link Optional} que contiene el proveedor encontrado,
     *         o {@link Optional#empty()} si no se encuentra ningún proveedor activo asociado a esa compañía.
     */
    Optional<Provider> findByActiveTrueAndCompany_NitCompany(String companyId);
}