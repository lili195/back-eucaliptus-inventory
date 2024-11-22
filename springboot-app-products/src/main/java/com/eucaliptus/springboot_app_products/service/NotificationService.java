package com.eucaliptus.springboot_app_products.service;

import com.eucaliptus.springboot_app_products.dto.NotificationDTO;
import com.eucaliptus.springboot_app_products.model.Notification;
import com.eucaliptus.springboot_app_products.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getTopNotifications() {
        return notificationRepository.findTop15ByOrderByDateDesc();
    }

}