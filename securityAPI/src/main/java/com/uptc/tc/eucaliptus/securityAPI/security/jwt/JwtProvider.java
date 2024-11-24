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

/**
 * Componente encargado de la generación y validación de tokens JWT.
 * <p>
 * Esta clase proporciona métodos para generar tokens JWT de acceso, validar su validez, obtener información
 * de los tokens, y manejar las excepciones relacionadas con JWT. Utiliza una clave secreta para firmar y
 * verificar los tokens, así como una fecha de expiración configurada.
 * </p>
 */

@Component
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret.key}")
    private String secretKey;
    @Value("${jwt.time.expiration}")
    private String timeExpiration;

    /**
     * Genera un token JWT de acceso.
     * <p>
     * Este método genera un token JWT que incluye el nombre de usuario, el rol y el correo electrónico del usuario,
     * y lo firma con la clave secreta configurada. El token tiene una fecha de expiración definida por la configuración.
     * </p>
     *
     * @param username El nombre de usuario para el que se genera el token.
     * @param role El rol del usuario, que se agrega como una reclamación dentro del token.
     * @param email El correo electrónico del usuario, que también se agrega como una reclamación dentro del token.
     * @return El token JWT generado.
     */

    public String generateToken(String username, Role role, String email) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role.getRoleName().name());
        claims.put("email", email);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration)))
                .signWith(SignatureAlgorithm.HS256, getSignatureKey())
                .compact();
    }

    /**
     * Valida la validez de un token JWT.
     * <p>
     * Este método valida si un token JWT es válido comprobando su firma y si no ha expirado.
     * Si el token no es válido o ha expirado, se registran los errores correspondientes.
     * </p>
     *
     * @param token El token JWT que se desea validar.
     * @return {@code true} si el token es válido, {@code false} si no lo es.
     */

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

    /**
     * Extrae el nombre de usuario de un token JWT.
     * <p>
     * Este método obtiene el nombre de usuario que está almacenado como el sujeto del token.
     * </p>
     *
     * @param token El token JWT del cual se extraerá el nombre de usuario.
     * @return El nombre de usuario almacenado en el token.
     */

    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(getSignatureKey()).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Obtiene todas las reclamaciones de un token JWT.
     * <p>
     * Este método extrae todas las reclamaciones (información adicional) contenidas en el token JWT.
     * </p>
     *
     * @param token El token JWT del cual se extraerán las reclamaciones.
     * @return Las reclamaciones del token como un objeto {@link Claims}.
     */

    public Claims getAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSignatureKey()).parseClaimsJws(token).getBody();
    }

    /**
     * Obtiene la clave secreta utilizada para firmar y verificar los tokens JWT.
     * <p>
     * Este método decodifica la clave secreta desde la configuración y la utiliza para crear la clave de firma.
     * </p>
     *
     * @return La clave secreta para firmar los tokens JWT.
     */

    public Key getSignatureKey(){
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
