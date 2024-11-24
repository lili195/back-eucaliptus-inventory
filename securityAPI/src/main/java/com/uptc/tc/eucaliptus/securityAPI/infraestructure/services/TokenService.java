package com.uptc.tc.eucaliptus.securityAPI.infraestructure.services;

import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.TokenEntity;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.repositories.TokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Servicio para gestionar las operaciones relacionadas con los tokens de usuario.
 * <p>
 * Este servicio permite gestionar tokens, incluyendo su creación, verificación y eliminación.
 * </p>
 */

@Transactional
@Service
public class TokenService {

    private TokenRepository tokenRepository;

    /**
     * Constructor para inyectar la dependencia del repositorio de tokens.
     *
     * @param tokenRepository El repositorio que maneja las operaciones de persistencia de tokens.
     */

    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    /**
     * Obtiene un token específico desde la base de datos.
     *
     * @param token El valor del token que se desea obtener.
     * @return Un {@link Optional} que contiene la entidad del token si se encuentra, o vacío si no.
     */

    public Optional<TokenEntity> getToken(String token) {
        return tokenRepository.findByToken(token);
    }

    /**
     * Verifica si existe un token en la base de datos.
     *
     * @param token El valor del token que se desea verificar.
     * @return {@code true} si el token existe, {@code false} en caso contrario.
     */

    public boolean existsToken(String token) {
        return tokenRepository.existsByToken(token);
    }

    /**
     * Guarda un nuevo token en la base de datos.
     *
     * @param tokenEntity La entidad del token que se desea guardar.
     * @return La entidad del token guardada.
     */

    public TokenEntity save(TokenEntity tokenEntity) {
        return tokenRepository.save(tokenEntity);
    }

    /**
     * Elimina un token específico de la base de datos.
     *
     * @param token El valor del token que se desea eliminar.
     * @throws NoSuchElementException Si el token no existe en la base de datos.
     */

    public void delete(String token) {
        tokenRepository.delete(getToken(token).get());
    }

    /**
     * Elimina todos los tokens asociados a un usuario específico.
     *
     * @param userId El identificador del usuario cuyos tokens se desean eliminar.
     */

    public void deleteByUserId(String userId) {
        tokenRepository.deleteByUserId(userId);
    }

}
