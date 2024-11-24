package com.uptc.tc.eucaliptus.securityAPI.infraestructure.repositories;

import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio de acceso a los datos de los usuarios.
 * <p>
 * Esta interfaz extiende {@link JpaRepository} para proporcionar métodos CRUD
 * para manejar los objetos {@link User}. También define consultas personalizadas
 * para interactuar con los usuarios en la base de datos.
 * </p>
 *
 * @see User
 * @see JpaRepository
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * Busca un usuario en la base de datos por su nombre de usuario.
     * <p>
     * Esta consulta devuelve el usuario correspondiente al nombre de usuario proporcionado.
     * </p>
     *
     * @param username El nombre de usuario.
     * @return Un {@link Optional} que contiene el usuario si se encuentra, vacío si no.
     */

    Optional<User> findByUsername(String username);

    /**
     * Busca un usuario en la base de datos por su correo electrónico.
     * <p>
     * Esta consulta devuelve el usuario correspondiente al correo electrónico proporcionado.
     * </p>
     *
     * @param email El correo electrónico del usuario.
     * @return Un {@link Optional} que contiene el usuario si se encuentra, vacío si no.
     */

    Optional<User> findByEmail(String email);

    /**
     * Verifica si existe un usuario con el nombre de usuario especificado.
     * <p>
     * Esta consulta verifica si existe un usuario con el nombre de usuario proporcionado.
     * </p>
     *
     * @param username El nombre de usuario.
     * @return {@code true} si el usuario existe, {@code false} de lo contrario.
     */

    boolean existsByUsername(String username);

}
