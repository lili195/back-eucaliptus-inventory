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

@Configuration
@PropertySource("classpath:email.properties")
public class EmailConfig {
    @Value("${email.username}")
    private String username;

    @Value("${email.password}")
    private String password;

    @Value("${email.auth}")
    private String auth;

    @Value("${email.host}")
    private String host;

    @Value("${email.port}")
    private String port;

    @Value("${email.starttls.enable}")
    private String tlsenable;

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", auth);
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.starttls.enable", tlsenable);
        return properties;
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setJavaMailProperties(getMailProperties());
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        return mailSender;
    }

    @Bean
    public ResourceLoader getResourceLoader() {
        return new DefaultResourceLoader();
    }

}
