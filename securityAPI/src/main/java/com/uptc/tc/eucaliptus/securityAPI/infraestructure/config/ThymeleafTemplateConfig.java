package com.uptc.tc.eucaliptus.securityAPI.infraestructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * Clase de configuración para integrar Thymeleaf como motor de plantillas.
 * <p>
 * Configura el motor de plantillas Thymeleaf, incluyendo la resolución de plantillas y
 * opciones de codificación y caché.
 */

@Configuration
public class ThymeleafTemplateConfig {

    /**
     * Define un bean de tipo {@link SpringResourceTemplateResolver} para resolver plantillas Thymeleaf.
     * <p>
     * Especifica el prefijo, sufijo, modo de plantilla, codificación y configuración de caché
     * para las plantillas utilizadas en la aplicación.
     *
     * @return una instancia de {@link SpringResourceTemplateResolver} configurada.
     */

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("classpath:/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false);  // Puedes ajustar esto según tus necesidades
        return templateResolver;
    }

    /**
     * Define un bean de tipo {@link SpringTemplateEngine} para procesar plantillas Thymeleaf.
     * <p>
     * Configura el motor de plantillas para utilizar el {@link SpringResourceTemplateResolver}
     * definido y habilita el compilador Spring Expression Language (SpEL).
     *
     * @return una instancia de {@link SpringTemplateEngine} configurada.
     */

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }
}
