package com.eucaliptus.springboot_app_notifications.security;

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
public class JwtTokenUtil {

    @Value("${jwt.secret.key}")
    private String secretKey;

    // Obtener el username del token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Obtener una reclamación específica del token
    public <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSignatureKey()).parseClaimsJws(token).getBody();

    }

    public Key getSignatureKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Validar el token
    public Boolean validateToken(String token) {
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

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
