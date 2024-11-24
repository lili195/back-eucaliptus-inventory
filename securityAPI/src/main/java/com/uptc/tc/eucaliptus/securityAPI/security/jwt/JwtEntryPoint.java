package com.uptc.tc.eucaliptus.securityAPI.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Componente que implementa el {@link AuthenticationEntryPoint} para manejar errores de autenticación.
 * <p>
 * Esta clase es responsable de manejar los casos en los que un usuario no autenticado intenta acceder
 * a recursos protegidos. En caso de error de autenticación, devuelve una respuesta con el estado HTTP
 * 401 (No autorizado) y un mensaje adecuado.
 * </p>
 */
@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {

    /**
     * Responde a una solicitud de autenticación no válida o no autorizada.
     * <p>
     * Este método se ejecuta cuando un usuario intenta acceder a un recurso protegido sin estar autenticado.
     * En tal caso, se envía un error con el código de estado 401 (No autorizado) y un mensaje explicativo.
     * </p>
     *
     * @param req La solicitud HTTP entrante.
     * @param res La respuesta HTTP que se enviará de vuelta al cliente.
     * @param e La excepción de autenticación que ha causado este error.
     * @throws IOException Si hay un error al escribir la respuesta.
     */

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException e)
            throws IOException {
        res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No autorizado");
    }
}
