package com.uptc.tc.eucaliptus.securityAPI.infraestructure.repositories;

import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.RecoveryCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RecoveryCodeRepository extends JpaRepository<RecoveryCode, String> {

    Optional<RecoveryCode> findByCodeAndExpiryDateAfter(int code, LocalDateTime now);

    Optional<RecoveryCode> findByUserId(String userId);

    Optional<RecoveryCode> findByCode(int code);

    boolean existsByCode(int code);

    boolean existsByCodeAndUserId(int code, String userId);

    @Modifying
    @Transactional
    void deleteRecoveryCodeByExpiryDateBefore(LocalDateTime now);

    @Modifying
    @Transactional
    void deleteRecoveryCodesByUserId(String userId);

}
