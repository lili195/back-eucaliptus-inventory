package com.uptc.tc.eucaliptus.securityAPI.infraestructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Clase de configuración para el servicio de correo electrónico.
 * <p>
 * Carga las propiedades del archivo `email.properties` para configurar
 * el envío de correos electrónicos mediante JavaMailSender.
 */

@Configuration
@PropertySource("classpath:email.properties")
public class EmailConfig {

    /** Nombre de usuario para el servidor de correo electrónico. */

    @Value("${email.username}")
    private String username;

    /** Contraseña para autenticación del correo electrónico. */

    @Value("${email.password}")
    private String password;

    /** Indicador de autenticación SMTP. */

    @Value("${email.auth}")
    private String auth;

    /** Dirección del servidor SMTP. */

    @Value("${email.host}")
    private String host;

    /** Puerto del servidor SMTP. */

    @Value("${email.port}")
    private String port;

    /** Indicador de habilitación de STARTTLS. */

    @Value("${email.starttls.enable}")
    private String tlsenable;

    /**
     * Configura las propiedades necesarias para el envío de correos electrónicos.
     *
     * @return un objeto {@link Properties} con las configuraciones de correo.
     */

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", auth);
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.starttls.enable", tlsenable);
        return properties;
    }

    /**
     * Define un bean de tipo {@link JavaMailSender} para manejar el envío de correos electrónicos.
     *
     * @return una instancia configurada de {@link JavaMailSender}.
     */

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setJavaMailProperties(getMailProperties());
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        return mailSender;
    }

    /**
     * Define un bean de tipo {@link ResourceLoader} para cargar recursos en la aplicación.
     *
     * @return una instancia de {@link DefaultResourceLoader}.
     */

    @Bean
    public ResourceLoader getResourceLoader() {
        return new DefaultResourceLoader();
    }

}
