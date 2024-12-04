package com.eucaliptus.springboot_app_person.security;

import com.eucaliptus.springboot_app_person.utlities.ServicesUri;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

/**
 * Configuración de seguridad para la aplicación.
 * Esta clase se encarga de configurar los aspectos relacionados con la autenticación, autorización, CORS,
 * y la gestión de sesiones para la aplicación web utilizando Spring Security.
 *
 * - Habilita CORS (Cross-Origin Resource Sharing) para permitir solicitudes desde orígenes específicos.
 * - Desactiva CSRF (Cross-Site Request Forgery) debido a la naturaleza sin estado de la aplicación (basada en tokens JWT).
 * - Configura un filtro de seguridad personalizado para procesar los tokens JWT.
 * - Configura la gestión de contraseñas mediante el uso de un codificador BCrypt.
 * - Configura la política de sesiones a "sin estado", ya que la aplicación utiliza autenticación basada en tokens.
 *
 * También habilita la seguridad de métodos mediante la anotación `@EnableMethodSecurity` para proteger
 * métodos específicos con anotaciones de seguridad (como `@PreAuthorize`).
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    /**
     * Crea un bean del filtro de autenticación de JWT que se ejecutará antes de la autenticación por nombre de usuario y contraseña.
     *
     * @return Un nuevo filtro `JwtRequestFilter`.
     */
    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter();
    }

    /**
     * Configura las reglas de seguridad HTTP.
     * - Habilita CORS con un origen específico y métodos permitidos.
     * - Desactiva CSRF debido a la autenticación basada en tokens JWT.
     * - Requiere autenticación para todas las solicitudes entrantes.
     * - Configura la política de creación de sesiones como `STATELESS` (sin estado).
     * - Añade el filtro de JWT antes del filtro de autenticación por nombre de usuario y contraseña.
     *
     * @param http La configuración de seguridad HTTP.
     * @return La cadena de filtros de seguridad configurada.
     * @throws Exception Si ocurre un error durante la configuración de seguridad.
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(List.of(ServicesUri.FRONT_URL));
                    configuration.setAllowedMethods(List.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                    configuration.setAllowCredentials(true);
                    configuration.addExposedHeader("Message");
                    configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
                    return configuration;
                }))
            .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Añadir el filtro de JWT antes del filtro de autenticación por token
        http.addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Crea un bean para el codificador de contraseñas utilizando BCrypt.
     *
     * @return Un nuevo codificador de contraseñas `BCryptPasswordEncoder`.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Crea un bean para el `AuthenticationManager` que se utiliza para manejar la autenticación de los usuarios.
     *
     * @param authenticationConfiguration La configuración de autenticación.
     * @return El `AuthenticationManager` configurado.
     * @throws Exception Si ocurre un error durante la configuración del `AuthenticationManager`.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
