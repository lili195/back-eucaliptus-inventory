package com.eucaliptus.springboot_app_products.mappers;

import com.eucaliptus.springboot_app_products.dto.NotificationDTO;
import com.eucaliptus.springboot_app_products.model.Notification;

public class NotificationMapper {

    public static NotificationDTO notificationToNotificationDTO(Notification notification) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setIdNotification(notification.getId());
        notificationDTO.setIdProduct(notification.getIdProduct());
        notificationDTO.setIdStock(notification.getIdStock());
        notificationDTO.setNotificationDate(notification.getDate());
        notificationDTO.setMessage(notification.getMessage());
        return notificationDTO;
    }
}
