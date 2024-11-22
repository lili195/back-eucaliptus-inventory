package com.eucaliptus.springboot_app_notifications.mappers;

import com.eucaliptus.springboot_app_notifications.dto.NotificationDTO;
import com.eucaliptus.springboot_app_notifications.model.Notification;

public class NotificationMapper {

    public static NotificationDTO notificationToNotificationDTO(Notification notification) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setIdNotification(notification.getIdNotification());
        notificationDTO.setMessage(notification.getMessage());
        notificationDTO.setIdStock(notification.getIdStock());
        notificationDTO.setIdProduct(notification.getIdProduct());
        notificationDTO.setNotificationDate(notification.getNotificationDate());
        return notificationDTO;
    }

    public static Notification notificationDTOToNotification(NotificationDTO notificationDTO) {
        Notification notification = new Notification();
        notification.setIdNotification(notificationDTO.getIdNotification());
        notification.setMessage(notificationDTO.getMessage());
        notification.setIdStock(notificationDTO.getIdStock());
        notification.setIdProduct(notificationDTO.getIdProduct());
        notification.setNotificationDate(notificationDTO.getNotificationDate());
        return notification;
    }
}

