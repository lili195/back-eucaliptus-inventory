package com.eucaliptus.springboot_app_person.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

/**
 * Utilidad para manejar operaciones relacionadas con el token JWT.
 * Proporciona métodos para extraer información del token, validarlo y obtener las reclamaciones del mismo.
 * El token se firma utilizando una clave secreta configurada en la propiedad {@code jwt.secret.key}.
 */
@Component
@Slf4j
public class JwtTokenUtil {

    @Value("${jwt.secret.key}")
    private String secretKey;

    /**
     * Extrae el nombre de usuario (subject) del token JWT.
     *
     * @param token El token JWT del que se extraerá el nombre de usuario.
     * @return El nombre de usuario extraído del token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrae una reclamación específica del token JWT.
     *
     * @param <T> El tipo de la reclamación a extraer.
     * @param token El token JWT del cual se extraerá la reclamación.
     * @param claimsResolver Función que define cómo extraer una reclamación específica del token.
     * @return El valor de la reclamación extraída.
     */
    public <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrae todas las reclamaciones del token JWT.
     *
     * @param token El token JWT del cual se extraerán las reclamaciones.
     * @return Las reclamaciones extraídas del token.
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSignatureKey()).parseClaimsJws(token).getBody();

    }

    /**
     * Obtiene la clave de firma utilizada para validar el token JWT.
     *
     * @return La clave de firma.
     */
    public Key getSignatureKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Valida si el token JWT es válido. Esto incluye verificar su firma y su expiración.
     *
     * @param token El token JWT que se validará.
     * @return {@code true} si el token es válido, {@code false} si no lo es.
     */
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

    /**
     * Verifica si el token JWT ha expirado.
     *
     * @param token El token JWT que se verificará.
     * @return {@code true} si el token ha expirado, {@code false} si no lo ha hecho.
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrae la fecha de expiración del token JWT.
     *
     * @param token El token JWT del cual se extraerá la fecha de expiración.
     * @return La fecha de expiración del token.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
