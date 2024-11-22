package com.eucaliptus.springboot_app_products.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
public class NotificationDTO {

    private Integer idNotification;
    private String message;
    private Date notificationDate;
    private Integer idStock;
    private String idProduct;

}
