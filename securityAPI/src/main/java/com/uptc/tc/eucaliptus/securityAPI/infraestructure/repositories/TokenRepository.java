package com.uptc.tc.eucaliptus.securityAPI.infraestructure.repositories;

import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, String> {

    Optional<TokenEntity> findByToken(String token);

    boolean existsByToken(String token);

    void deleteByUserId(String userId);
}
