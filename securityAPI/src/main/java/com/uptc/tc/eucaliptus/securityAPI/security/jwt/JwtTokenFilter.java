package com.uptc.tc.eucaliptus.securityAPI.security.jwt;

import com.uptc.tc.eucaliptus.securityAPI.infraestructure.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro personalizado para manejar la autenticación basada en JWT en las solicitudes HTTP.
 * <p>
 * Este filtro intercepta las solicitudes HTTP para extraer y validar el token JWT proporcionado en
 * el encabezado `Authorization`. Si el token es válido, extrae el nombre de usuario, carga los detalles
 * del usuario y establece la autenticación en el contexto de seguridad de Spring.
 * </p>
 */

public class JwtTokenFilter extends OncePerRequestFilter {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(JwtTokenFilter.class);
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    /**
     * Realiza el filtrado de las solicitudes HTTP para verificar la validez del token JWT.
     * <p>
     * Este método obtiene el token JWT del encabezado `Authorization`, lo valida utilizando
     * el {@link JwtProvider}, y si es válido, carga los detalles del usuario y establece la autenticación
     * en el contexto de seguridad de Spring.
     * </p>
     *
     * @param request La solicitud HTTP que contiene el token JWT en el encabezado `Authorization`.
     * @param response La respuesta HTTP.
     * @param filterChain La cadena de filtros que se debe ejecutar después de este filtro.
     * @throws ServletException Si ocurre un error durante el procesamiento de la solicitud.
     * @throws IOException Si ocurre un error de entrada/salida durante el filtrado.
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String tokenHeader = request.getHeader("Authorization");
            if(tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
                String token = tokenHeader.substring(7);
                if (jwtProvider.isTokenValid(token)) {
                    String username = jwtProvider.getUsernameFromToken(token);
                    UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null,
                            userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}