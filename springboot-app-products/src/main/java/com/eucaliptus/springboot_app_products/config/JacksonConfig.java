package com.eucaliptus.springboot_app_products.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Clase de configuración para Jackson que personaliza el comportamiento
 * del mapeo de objetos JSON, incluyendo el manejo de fechas y horas.
 *
 * <p>Esta configuración se utiliza para registrar módulos adicionales
 * y ajustar la serialización/deserialización de fechas y horas en formato JSON.</p>
 */

@Configuration
public class JacksonConfig {

    /**
     * Crea y configura un bean de tipo {@link ObjectMapper}.
     *
     * <p>Se registra el módulo {@link JavaTimeModule} para habilitar el soporte
     * de las nuevas clases de fecha y hora de Java 8. También se desactiva
     * la escritura de fechas como timestamps para que las fechas se serialicen
     * en un formato ISO-8601 legible.</p>
     *
     * @return un objeto {@link ObjectMapper} configurado.
     */

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }

}
