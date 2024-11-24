package com.uptc.tc.eucaliptus.securityAPI.security;

import com.uptc.tc.eucaliptus.securityAPI.security.jwt.JwtEntryPoint;
import com.uptc.tc.eucaliptus.securityAPI.security.jwt.JwtTokenFilter;
import com.uptc.tc.eucaliptus.securityAPI.utlities.ServicesUri;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

/**
 * Configuración de seguridad para la aplicación web.
 * <p>
 * Esta clase configura la seguridad de la aplicación, habilitando la autenticación basada en JWT,
 * la protección contra CSRF, CORS, y establece políticas de sesión sin estado. También configura
 * el manejo de contraseñas utilizando {@link BCryptPasswordEncoder} y el filtro de tokens JWT.
 * </p>
 *
 * @see JwtTokenFilter
 * @see JwtEntryPoint
 * @see BCryptPasswordEncoder
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Autowired
    private JwtEntryPoint jwtEntryPoint;

    /**
     * Crea un filtro para manejar los tokens JWT en las solicitudes entrantes.
     *
     * @return Un filtro de tokens JWT que intercepta las solicitudes para validar la autenticación.
     */

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter();
    }

    /**
     * Crea un codificador de contraseñas basado en BCrypt.
     * <p>
     * El {@link BCryptPasswordEncoder} es utilizado para cifrar las contraseñas de los usuarios
     * de forma segura antes de almacenarlas en la base de datos.
     * </p>
     *
     * @return Un {@link PasswordEncoder} configurado para usar el algoritmo BCrypt.
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Crea un {@link AuthenticationManager} para gestionar la autenticación de los usuarios.
     * <p>
     * El {@link AuthenticationManager} es utilizado para validar las credenciales de los usuarios
     * durante el proceso de inicio de sesión.
     * </p>
     *
     * @param authenticationConfiguration Configuración de autenticación proporcionada por Spring.
     * @return Un {@link AuthenticationManager} para la autenticación de los usuarios.
     * @throws Exception Si hay un error al obtener el {@link AuthenticationManager}.
     */

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configura las reglas de seguridad para las solicitudes HTTP de la aplicación.
     * <p>
     * Este método configura CORS, desactiva CSRF, define las rutas públicas permitidas, establece el
     * manejo de excepciones de autenticación, y configura la política de creación de sesiones
     * para ser sin estado (stateless).
     * </p>
     *
     * @param http La configuración de seguridad HTTP de Spring.
     * @return Un {@link SecurityFilterChain} que contiene las reglas de seguridad para las solicitudes.
     * @throws Exception Si hay un error durante la configuración de seguridad.
     */

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(List.of(ServicesUri.FRONT_URL,
                            "https://dev-store-demo.web.app",
                            "https://dev-store-demo.firebaseapp.com/"));
                    configuration.setAllowedMethods(List.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                    configuration.setAllowCredentials(true);
                    configuration.addExposedHeader("Message");
                    configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
                    return configuration;
                }))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login",
                                "/auth/requestRecoveryPassword/*",
                                "/auth/validateRecoveryCode",
                                "/auth/recoveryPassword").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}