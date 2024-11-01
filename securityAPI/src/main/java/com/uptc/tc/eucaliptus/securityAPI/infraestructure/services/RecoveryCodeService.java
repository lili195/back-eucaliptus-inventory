package com.uptc.tc.eucaliptus.securityAPI.infraestructure.services;

import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.RecoveryCode;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.repositories.RecoveryCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class RecoveryCodeService {

    @Autowired
    private RecoveryCodeRepository recoveryCodeRepository;

    public RecoveryCode saveNewRecoveryCode(RecoveryCode recoveryCode) {
        recoveryCodeRepository.deleteRecoveryCodesByUserId(recoveryCode.getUser().getId());
        return recoveryCodeRepository.save(recoveryCode);
    }

    public Optional<RecoveryCode> findByCode(int code) {
        return recoveryCodeRepository.findByCode(code);
    }

    public boolean existsByCodeAndUser(int code, String userId) {
        return recoveryCodeRepository.existsByCodeAndUserId(code, userId);
    }

    public void deleteExpiratedCodes(){
        recoveryCodeRepository.deleteRecoveryCodeByExpiryDateBefore(LocalDateTime.now());
    }

    public void deleteByCode(int code) {
        recoveryCodeRepository.delete(recoveryCodeRepository.findByCode(code).get());
    }

    public LocalDateTime getExpirationDate(){
        return LocalDateTime.now().plusMinutes(10);
    }

    public int generateCode(){
        int code = new Random().nextInt(999999);
        while(recoveryCodeRepository.existsByCode(code))
            code = new Random().nextInt(999999);
        return code;
    }

}
