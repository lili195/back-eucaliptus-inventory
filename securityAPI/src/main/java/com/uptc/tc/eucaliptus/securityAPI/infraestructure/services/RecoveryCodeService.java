package com.uptc.tc.eucaliptus.securityAPI.infraestructure.services;

import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.RecoveryCode;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.repositories.RecoveryCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

/**
 * Servicio para manejar las operaciones relacionadas con los códigos de recuperación.
 * <p>
 * Esta clase proporciona métodos para generar, guardar, buscar y eliminar códigos de recuperación,
 * así como para manejar su expiración.
 * </p>
 */

@Service
public class RecoveryCodeService {

    @Autowired
    private RecoveryCodeRepository recoveryCodeRepository;

    /**
     * Guarda un nuevo código de recuperación para un usuario.
     * <p>
     * Si ya existen códigos de recuperación asociados al usuario, estos se eliminan antes de guardar el nuevo código.
     * </p>
     *
     * @param recoveryCode El código de recuperación que se va a guardar.
     * @return El código de recuperación guardado.
     */

    public RecoveryCode saveNewRecoveryCode(RecoveryCode recoveryCode) {
        recoveryCodeRepository.deleteRecoveryCodesByUserId(recoveryCode.getUser().getId());
        return recoveryCodeRepository.save(recoveryCode);
    }

    /**
     * Busca un código de recuperación por su valor.
     *
     * @param code El valor del código de recuperación a buscar.
     * @return Un {@link Optional} que contiene el código de recuperación si se encuentra.
     */

    public Optional<RecoveryCode> findByCode(int code) {
        return recoveryCodeRepository.findByCode(code);
    }

    /**
     * Verifica si existe un código de recuperación asociado a un usuario.
     *
     * @param code El valor del código de recuperación.
     * @param userId El identificador del usuario.
     * @return {@code true} si el código existe para el usuario, {@code false} en caso contrario.
     */

    public boolean existsByCodeAndUser(int code, String userId) {
        return recoveryCodeRepository.existsByCodeAndUserId(code, userId);
    }

    /**
     * Elimina todos los códigos de recuperación que hayan expirado.
     */

    public void deleteExpiratedCodes(){
        recoveryCodeRepository.deleteRecoveryCodeByExpiryDateBefore(LocalDateTime.now());
    }

    /**
     * Elimina un código de recuperación específico.
     *
     * @param code El valor del código de recuperación a eliminar.
     */

    public void deleteByCode(int code) {
        recoveryCodeRepository.delete(recoveryCodeRepository.findByCode(code).get());
    }

    /**
     * Obtiene la fecha de expiración estándar para los códigos de recuperación.
     * <p>
     * Por defecto, la fecha de expiración se establece en 10 minutos a partir del momento actual.
     * </p>
     *
     * @return La fecha y hora de expiración.
     */

    public LocalDateTime getExpirationDate(){
        return LocalDateTime.now().plusMinutes(10);
    }

    /**
     * Genera un nuevo código de recuperación único.
     * <p>
     * El código generado es un número aleatorio de 6 dígitos que no existe previamente en el repositorio.
     * </p>
     *
     * @return Un código de recuperación único.
     */

    public int generateCode(){
        int code = new Random().nextInt(999999);
        while(recoveryCodeRepository.existsByCode(code))
            code = new Random().nextInt(999999);
        return code;
    }

}
