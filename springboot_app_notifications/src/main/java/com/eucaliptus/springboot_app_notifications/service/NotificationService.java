package com.eucaliptus.springboot_app_notifications.service;

import com.eucaliptus.springboot_app_notifications.dto.NotificationDTO;
import com.eucaliptus.springboot_app_notifications.mappers.NotificationMapper;
import com.eucaliptus.springboot_app_notifications.model.Notification;
import com.eucaliptus.springboot_app_notifications.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public List<NotificationDTO> getAllNotifications() {
        return notificationRepository.findAll()
                .stream()
                .map(NotificationMapper::notificationToNotificationDTO)
                .collect(Collectors.toList());
    }

    public void saveNotification(NotificationDTO notificationDTO) {
        Notification notification = NotificationMapper.notificationDTOToNotification(notificationDTO);
        notificationRepository.save(notification);
    }
}
