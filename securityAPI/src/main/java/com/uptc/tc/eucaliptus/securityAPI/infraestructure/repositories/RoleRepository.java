package com.uptc.tc.eucaliptus.securityAPI.infraestructure.repositories;

import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.Role;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.enums.RoleList;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio de acceso a los datos de los roles de usuario.
 * <p>
 * Esta interfaz extiende {@link JpaRepository} para proporcionar métodos CRUD
 * para manejar los objetos {@link Role}. Además, define consultas personalizadas
 * para interactuar con los roles en la base de datos.
 * </p>
 *
 * @see Role
 * @see JpaRepository
 */

public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Busca un rol en la base de datos por su nombre.
     * <p>
     * Esta consulta devuelve el rol correspondiente al nombre de rol proporcionado.
     * </p>
     *
     * @param roleName El nombre del rol.
     * @return Un {@link Optional} que contiene el rol si se encuentra, vacío si no.
     */

    Optional<Role> findByRoleName(@NotNull RoleList roleName);

    /**
     * Verifica si existe un rol con el nombre especificado.
     * <p>
     * Esta consulta verifica si existe un rol con el nombre de rol proporcionado.
     * </p>
     *
     * @param roleName El nombre del rol.
     * @return {@code true} si el rol existe, {@code false} de lo contrario.
     */

    boolean existsByRoleName(@NotNull RoleList roleName);
}
