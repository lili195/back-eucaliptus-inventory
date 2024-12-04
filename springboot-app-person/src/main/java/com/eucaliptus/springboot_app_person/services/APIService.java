package com.eucaliptus.springboot_app_person.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;


/**
 * Servicio encargado de manejar la autenticación basada en tokens.
 * Esta clase proporciona métodos para extraer el token de la cabecera de autorización de una solicitud HTTP
 * y para generar las cabeceras necesarias con el token para futuras solicitudes.
 *
 * Los métodos proporcionados permiten:
 * - Obtener el token desde la cabecera `Authorization` de la solicitud.
 * - Crear un objeto `HttpHeaders` con el token para agregarlo a las solicitudes HTTP.
 */
@Service
public class APIService {

    /**
     * Extrae el token JWT de la cabecera de autorización de la solicitud HTTP.
     *
     * Este método busca la cabecera `Authorization` en la solicitud y, si está presente y comienza con "Bearer ",
     * extrae el token que sigue. El token debe estar en el formato estándar de "Bearer token".
     *
     * @param request La solicitud HTTP que contiene la cabecera de autorización.
     * @return El token JWT extraído de la cabecera, o {@code null} si no se encuentra un token válido.
     */
    public String getTokenByRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer "))
            token = authHeader.substring(7);
        return token;
    }

    /**
     * Crea y devuelve un objeto `HttpHeaders` que incluye el token de autorización.
     *
     * Este método es útil cuando se necesita agregar un token de autenticación a las cabeceras de una solicitud HTTP.
     * El token se incluye en la cabecera `Authorization` utilizando el esquema "Bearer ".
     *
     * @param token El token JWT que se agregará a las cabeceras de la solicitud.
     * @return El objeto `HttpHeaders` con la cabecera `Authorization` configurada con el token.
     */
    public HttpHeaders getHeader(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }
}
