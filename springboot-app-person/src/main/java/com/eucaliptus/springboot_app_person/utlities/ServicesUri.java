package com.eucaliptus.springboot_app_person.utlities;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Clase que gestiona las URLs de los servicios externos en la aplicación.
 * Esta clase se encarga de almacenar las URL de los servicios de autenticación,
 * productos y el frontend, los cuales son configurados en los archivos de propiedades de la aplicación.
 *
 * Estas URLs son utilizadas por otros componentes del sistema para realizar solicitudes a dichos servicios.
 */
@Component
public class ServicesUri {

    /**
     * URL del servicio de autenticación.
     */
    public static String AUTH_SERVICE;
    /**
     * URL del servicio de productos.
     */
    public static String PRODUCT_SERVICE;
    /**
     * URL base del frontend.
     */
    public static String FRONT_URL;


    /**
     * Valor de la URL de autenticación que se inyecta desde el archivo de configuración.
     */
    @Value("${services.authentication}")
    private String authentication;

    /**
     * Valor de la URL del servicio de productos que se inyecta desde el archivo de configuración.
     */
    @Value("${services.product}")
    private String product;

    /**
     * Valor de la URL base del frontend que se inyecta desde el archivo de configuración.
     */
    @Value("${services.front.baseUrl}")
    private String front;

    /**
     * Método de inicialización que asigna los valores inyectados a las variables estáticas de la clase.
     * Este método es ejecutado automáticamente después de que las propiedades sean inyectadas.
     */
    @PostConstruct
    public void init() {
        AUTH_SERVICE = authentication;
        PRODUCT_SERVICE = product;
        FRONT_URL = front;
    }
}