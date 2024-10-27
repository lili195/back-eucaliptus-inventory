package com.uptc.tc.eucaliptus.securityAPI.utlities;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServicesUri {

    public static String FRONT_URL;
    public static String PERSON_SERVICE;

    @Value("${services.front.baseUrl}")
    private String front;

    @Value("${services.person.baseUrl}")
    private String person;

    @PostConstruct
    public void init() {
        FRONT_URL = front;
        PERSON_SERVICE = person;
    }


}
