package com.uptc.tc.eucaliptus.securityAPI.infraestructure.repositories;

import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio de acceso a los datos de los tokens de usuario.
 * <p>
 * Esta interfaz extiende {@link JpaRepository} para proporcionar métodos CRUD
 * para manejar los objetos {@link TokenEntity}. Además, define consultas personalizadas
 * para interactuar con los tokens en la base de datos.
 * </p>
 *
 * @see TokenEntity
 * @see JpaRepository
 */

public interface TokenRepository extends JpaRepository<TokenEntity, String> {

    /**
     * Busca un token en la base de datos por su valor.
     * <p>
     * Esta consulta devuelve el token correspondiente al valor de token proporcionado.
     * </p>
     *
     * @param token El valor del token.
     * @return Un {@link Optional} que contiene el token si se encuentra, vacío si no.
     */

    Optional<TokenEntity> findByToken(String token);

    /**
     * Verifica si existe un token con el valor especificado.
     * <p>
     * Esta consulta verifica si existe un token con el valor de token proporcionado.
     * </p>
     *
     * @param token El valor del token.
     * @return {@code true} si el token existe, {@code false} de lo contrario.
     */

    boolean existsByToken(String token);

    /**
     * Elimina los tokens asociados a un usuario.
     * <p>
     * Elimina todos los tokens en la base de datos asociados al usuario especificado.
     * </p>
     *
     * @param userId El identificador del usuario cuyo token se eliminará.
     */

    void deleteByUserId(String userId);
}
