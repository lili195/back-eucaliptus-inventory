package com.uptc.tc.eucaliptus.securityAPI.security.jwt;

import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret.key}")
    private String secretKey;
    @Value("${jwt.time.expiration}")
    private String timeExpiration;

    // Generar token de acceso
    public String generateToken(String username, Role role) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role.getRoleName().name());
        return Jwts.builder()
                .setClaims(claims)
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
            log.error("TokenEntity mal formado");
        } catch (UnsupportedJwtException e) {
            log.error("TokenEntity no soportado");
        } catch (ExpiredJwtException e) {
            log.error("TokenEntity expirado");
        } catch (IllegalArgumentException e) {
            log.error("TokenEntity vacío");
        } catch (SignatureException e) {
            log.error("Fallo en la firma");
        }
        return false;
    }

    // Obtener usuario del token
    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(getSignatureKey()).parseClaimsJws(token).getBody().getSubject();
    }

    public Claims getAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSignatureKey()).parseClaimsJws(token).getBody();
    }

    // Obtener la llave de seguridad codificada
    public Key getSignatureKey(){
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
