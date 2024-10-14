package com.uptc.tc.eucaliptus.securityAPI.infraestructure.services;

import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.TokenEntity;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.repositories.TokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Transactional
@Service
public class TokenService {

    private TokenRepository tokenRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Optional<TokenEntity> getToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public boolean existsToken(String token) {
        return tokenRepository.existsByToken(token);
    }

    public TokenEntity save(TokenEntity tokenEntity) {
        return tokenRepository.save(tokenEntity);
    }

    public void delete(String token) {
        tokenRepository.delete(getToken(token).get());
    }

    public void deleteByUserId(String userId) {
        tokenRepository.deleteByUserId(userId);
    }

}
