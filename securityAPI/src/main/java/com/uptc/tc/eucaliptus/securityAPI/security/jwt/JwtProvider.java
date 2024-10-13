package com.uptc.tc.eucaliptus.securityAPI.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret.key}")
    private String secretKey;
    @Value("${jwt.time.expiration}")
    private String timeExpiration;

    // Generar token de acceso
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration)))
                .signWith(SignatureAlgorithm.HS256, getSignatureKey())
                .compact();
    }

    // Validar la validez del token
    public boolean isTokenValid(String token){
        try {
            Jwts.parser().setSigningKey(getSignatureKey()).parseClaimsJws(token).getBody();
            return true;
        } catch (MalformedJwtException e) {
            log.error("Token mal formado");
        } catch (UnsupportedJwtException e) {
            log.error("Token no soportado");
        } catch (ExpiredJwtException e) {
            log.error("Token expirado");
        } catch (IllegalArgumentException e) {
            log.error("Token vacío");
        } catch (SignatureException e) {
            log.error("Fallo en la firma");
        }
        return false;
    }

    // Obtener usuario del token
    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(getSignatureKey()).parseClaimsJws(token).getBody().getSubject();
    }

    // Obtener la llave de seguridad codificada
    public Key getSignatureKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
