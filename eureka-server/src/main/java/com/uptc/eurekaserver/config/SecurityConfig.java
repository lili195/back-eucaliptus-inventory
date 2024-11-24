package com.uptc.eurekaserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Clase de configuración de seguridad para el servidor Eureka.
 * <p>
 * Esta clase desactiva la protección CSRF (Cross-Site Request Forgery)
 * y configura la cadena de filtros de seguridad de Spring Security.
 * </p>
 */
@Configuration
public class SecurityConfig {

    /**
     * Configura la cadena de filtros de seguridad para el servidor.
     * <p>
     * En esta configuración, se deshabilita el soporte para CSRF.
     * </p>
     *
     * @param http instancia de {@link HttpSecurity} utilizada para personalizar la seguridad.
     * @return la cadena de filtros de seguridad configurada.
     * @throws Exception si ocurre un error durante la configuración.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}

