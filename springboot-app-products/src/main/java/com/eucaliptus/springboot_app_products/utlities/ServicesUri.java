package com.eucaliptus.springboot_app_products.utlities;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServicesUri {

    public static String AUTH_SERVICE;
    public static String PERSON_SERVICE;
    public static String FRONT_URL;

    @Value("${services.authentication}")
    private String authentication;

    @Value("${services.front.baseUrl}")
    private String front;

    @Value("${services.person}")
    private String person;

    @PostConstruct
    public void init() {
        AUTH_SERVICE = authentication;
        PERSON_SERVICE = person;
        FRONT_URL = front;
    }


}
