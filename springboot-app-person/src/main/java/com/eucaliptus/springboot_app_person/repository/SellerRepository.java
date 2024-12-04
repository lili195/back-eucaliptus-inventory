package com.eucaliptus.springboot_app_person.repository;

import com.eucaliptus.springboot_app_person.enums.EnumRole;
import com.eucaliptus.springboot_app_person.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para acceder y manejar entidades de tipo {@link Seller} en la base de datos.
 *
 * La interfaz {@link SellerRepository} extiende {@link JpaRepository}, proporcionando operaciones CRUD
 * sobre las entidades {@link Seller}. Además, incluye métodos específicos para buscar vendedores por su
 * número de identificación, por su nombre de usuario, por su rol y estado activo.
 */

@Repository
public interface SellerRepository extends JpaRepository<Seller, String> {

    /**
     * Verifica si existe un vendedor con el número de identificación proporcionado.
     *
     * @param id El número de identificación del vendedor que se desea verificar.
     * @return {@code true} si existe un vendedor con el número de identificación proporcionado;
     *         {@code false} si no existe.
     */
    boolean existsByIdNumber(String id);

    /**
     * Verifica si existe un vendedor con el nombre de usuario proporcionado.
     *
     * @param Username El nombre de usuario del vendedor que se desea verificar.
     * @return {@code true} si existe un vendedor con el nombre de usuario proporcionado; 
     *         {@code false} si no existe.
     */
    boolean existsByUsername(String Username);

    /**
     * Encuentra todos los vendedores que están activos y que tienen un rol específico.
     *
     * @param role El rol que se desea filtrar para los vendedores.
     * @return Una lista de vendedores activos con el rol especificado.
     */
    List<Seller> findByActiveTrueAndRole(EnumRole role);

    /**
     * Encuentra un vendedor por su número de identificación.
     *
     * @param personId El número de identificación del vendedor que se desea buscar.
     * @return Un {@link Optional} que contiene el vendedor encontrado,
     *         o {@link Optional#empty()} si no se encuentra ningún vendedor con ese número de identificación.
     */
    Optional<Seller> findByIdNumber(String personId);

    /**
     * Encuentra un vendedor por su nombre de usuario.
     *
     * @param username El nombre de usuario del vendedor que se desea buscar.
     * @return Un {@link Optional} que contiene el vendedor encontrado,
     *         o {@link Optional#empty()} si no se encuentra ningún vendedor con ese nombre de usuario.
     */
    Optional<Seller> getByUsername(String username);
}