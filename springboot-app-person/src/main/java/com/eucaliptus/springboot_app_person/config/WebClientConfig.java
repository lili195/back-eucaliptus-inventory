package com.eucaliptus.springboot_app_person.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuración de WebClient para la aplicación.
 * <p>
 * Esta clase configura un bean de tipo {@link WebClient.Builder}, el cual es utilizado
 * para crear instancias de {@link WebClient} que permiten realizar peticiones HTTP reactivas.
 * </p>
 * La configuración es gestionada por Spring y el bean se inyectará donde sea necesario para
 * facilitar la comunicación con otros servicios a través de HTTP.
 */
@Configuration
public class WebClientConfig {

    /**
     * Crea un {@link WebClient.Builder} que se puede utilizar para configurar y construir instancias de {@link WebClient}.
     *
     * @return un {@link WebClient.Builder} para crear instancias de {@link WebClient}.
     */
    @Bean
    public WebClient.Builder builder(){
        return WebClient.builder();
    }
}
