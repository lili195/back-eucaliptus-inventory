package com.eucaliptus.springboot_app_person.security;

import com.eucaliptus.springboot_app_person.utlities.ServicesUri;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;


/**
 * Filtro que intercepta las solicitudes HTTP y extrae el token JWT del encabezado "Authorization".
 * Valida el token JWT y, si es válido, establece la autenticación del usuario en el contexto de seguridad de Spring.
 *
 * Este filtro se ejecuta una sola vez por solicitud (a través de la clase {@link OncePerRequestFilter}) y es
 * responsable de autenticar a los usuarios mediante tokens JWT en las solicitudes HTTP entrantes.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private RestTemplate restTemplate;

    /**
     * Método que se ejecuta en cada solicitud para filtrar la autenticación.
     * Extrae el token JWT del encabezado "Authorization", lo valida, y si es válido, configura la autenticación
     * del usuario en el contexto de seguridad de Spring.
     *
     * @param request La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @param filterChain La cadena de filtros que permite que la solicitud continúe.
     * @throws ServletException Si ocurre un error en el procesamiento de la solicitud.
     * @throws IOException Si ocurre un error en la entrada/salida de la solicitud.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String tokenHeader = request.getHeader("Authorization");
            if(tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
                String token = tokenHeader.substring(7);
                //if (isValidToken(token)) {
                    String username = jwtTokenUtil.extractUsername(token);
                    Collection<? extends GrantedAuthority> authorities = Collections.singletonList(
                            new SimpleGrantedAuthority(jwtTokenUtil.extractAllClaims(token).get("role", String.class))
                    );
                    UserDetails userDetails = new User(username, username, authorities);
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null,
                            userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                //}
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Método para validar el token JWT realizando una solicitud HTTP a un servicio de autenticación.
     *
     * @param token El token JWT que se desea validar.
     * @return {@code true} si el token es válido, {@code false} si no lo es.
     */

    private boolean isValidToken(String token) {
        HttpEntity<String> entity = new HttpEntity<>(token, getHeader(token).getHeaders());
        ResponseEntity<Boolean> response = restTemplate.exchange(
                ServicesUri.AUTH_SERVICE + "/auth/validate",
                HttpMethod.POST,
                entity,
                Boolean.class
        );
        return response.getBody();
    }

    /**
     * Método para crear los encabezados HTTP necesarios para enviar el token JWT en una solicitud.
     *
     * @param token El token JWT que se desea incluir en los encabezados.
     * @return Un objeto {@link HttpEntity} que contiene los encabezados con el token JWT.
     */

    private HttpEntity<String> getHeader(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return new HttpEntity<>(headers);
    }
}

