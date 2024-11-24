package com.uptc.tc.eucaliptus.securityAPI.infraestructure.repositories;

import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.RecoveryCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repositorio de acceso a los datos de los códigos de recuperación.
 * <p>
 * Esta interfaz extiende JpaRepository para proporcionar métodos CRUD
 * para manejar los objetos {@link RecoveryCode}. Además, define consultas
 * personalizadas para interactuar con los códigos de recuperación en la base de datos.
 * </p>
 *
 * @see RecoveryCode
 * @see JpaRepository
 */

@Repository
public interface RecoveryCodeRepository extends JpaRepository<RecoveryCode, String> {

    /**
     * Busca un código de recuperación que no haya expirado.
     * <p>
     * Esta consulta devuelve un código de recuperación que coincide con el código proporcionado
     * y cuya fecha de expiración es posterior a la fecha y hora proporcionadas.
     * </p>
     *
     * @param code El código de recuperación.
     * @param now  La fecha y hora actuales.
     * @return Un {@link Optional} que contiene el código de recuperación si se encuentra, vacío si no.
     */

    Optional<RecoveryCode> findByCodeAndExpiryDateAfter(int code, LocalDateTime now);

    /**
     * Busca un código de recuperación asociado con un usuario.
     * <p>
     * Esta consulta busca un código de recuperación que esté asociado al ID de usuario proporcionado.
     * </p>
     *
     * @param userId El ID del usuario.
     * @return Un {@link Optional} que contiene el código de recuperación si se encuentra, vacío si no.
     */

    Optional<RecoveryCode> findByUserId(String userId);

    /**
     * Busca un código de recuperación por su código.
     * <p>
     * Esta consulta devuelve un código de recuperación que coincide con el código proporcionado.
     * </p>
     *
     * @param code El código de recuperación.
     * @return Un {@link Optional} que contiene el código de recuperación si se encuentra, vacío si no.
     */

    Optional<RecoveryCode> findByCode(int code);

    /**
     * Verifica si un código de recuperación existe en la base de datos.
     * <p>
     * Esta consulta verifica si existe un código de recuperación con el valor especificado.
     * </p>
     *
     * @param code El código de recuperación.
     * @return {@code true} si el código de recuperación existe, {@code false} de lo contrario.
     */

    boolean existsByCode(int code);

    /**
     * Verifica si un código de recuperación existe para un usuario específico.
     * <p>
     * Esta consulta verifica si existe un código de recuperación asociado a un usuario
     * con el ID proporcionado y el código especificado.
     * </p>
     *
     * @param code   El código de recuperación.
     * @param userId El ID del usuario.
     * @return {@code true} si el código de recuperación existe para el usuario, {@code false} de lo contrario.
     */

    boolean existsByCodeAndUserId(int code, String userId);

    /**
     * Elimina los códigos de recuperación cuyo tiempo de expiración sea anterior al momento actual.
     * <p>
     * Esta operación de modificación eliminará los códigos de recuperación que han expirado, es decir,
     * aquellos cuyo campo es anterior a la fecha y hora proporcionada.
     * </p>
     *
     * @param now La fecha y hora actuales.
     */

    @Modifying
    @Transactional
    void deleteRecoveryCodeByExpiryDateBefore(LocalDateTime now);

    /**
     * Elimina todos los códigos de recuperación asociados a un usuario.
     * <p>
     * Esta operación de modificación eliminará todos los códigos de recuperación asociados
     * al ID de usuario proporcionado.
     * </p>
     *
     * @param userId El ID del usuario.
     */

    @Modifying
    @Transactional
    void deleteRecoveryCodesByUserId(String userId);

}
