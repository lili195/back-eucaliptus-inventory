package com.eucaliptus.springboot_app_products.service;

import com.eucaliptus.springboot_app_products.dto.NotificationDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {

    @Value("${notification.service.url}")
    private String notificationServiceUrl;

    private final RestTemplate restTemplate;

    public NotificationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendNotification(NotificationDTO notificationDTO) {
        String endpoint = notificationServiceUrl + "/api/notifications";
        restTemplate.postForEntity(endpoint, notificationDTO, String.class);
    }

}