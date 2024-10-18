package com.eucaliptus.springboot_app_person.utlities;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServicesUri {

    public static String AUTH_SERVICE;

    @Value("${services.authentication}")
    private String authentication;

    @PostConstruct
    public void init() {
        AUTH_SERVICE = authentication;
    }


}
