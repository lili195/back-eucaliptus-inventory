package com.eucaliptus.springboot_app_notifications.controllers;

import com.eucaliptus.springboot_app_notifications.dto.Message;
import com.eucaliptus.springboot_app_notifications.dto.NotificationDTO;
import com.eucaliptus.springboot_app_notifications.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Object> getAllNotifications() {
        try {
            List<NotificationDTO> notifications = notificationService.getAllNotifications();
            return new ResponseEntity<>(notifications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("Error al obtener las notificaciones. Intente más tarde."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Object> createNotification(@RequestBody NotificationDTO notificationDTO) {
        try {
            notificationService.saveNotification(notificationDTO);
            return ResponseEntity.ok(new Message("Notification received"));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Error al guardar las notificaciones. Intente más tarde."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
