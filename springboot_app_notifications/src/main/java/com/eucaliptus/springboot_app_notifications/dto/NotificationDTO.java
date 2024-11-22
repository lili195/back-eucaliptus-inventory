package com.eucaliptus.springboot_app_notifications.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Long idNotification;
    private Integer idStock;
    private String idProduct;
    private LocalDateTime notificationDate;
    private String message;

}
