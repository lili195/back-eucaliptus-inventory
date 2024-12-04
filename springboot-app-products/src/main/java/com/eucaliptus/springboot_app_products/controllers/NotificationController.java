package com.eucaliptus.springboot_app_products.controllers;

import com.eucaliptus.springboot_app_products.dto.Message;
import com.eucaliptus.springboot_app_products.mappers.NotificationMapper;
import com.eucaliptus.springboot_app_products.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

/**
 * Controlador REST para gestionar notificaciones relacionadas con el stock.
 *
 * <p>Este controlador proporciona un endpoint para obtener notificaciones
 * sobre el estado del stock de productos. Solo usuarios con roles específicos
 * tienen acceso a esta funcionalidad.</p>
 */

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * Endpoint para obtener las notificaciones principales sobre el stock de productos.
     *
     * <p>Este método está protegido mediante autorización, permitiendo acceso solo
     * a usuarios con los roles <code>ROLE_ADMIN</code> o <code>ROLE_SELLER</code>.</p>
     *
     * @return una respuesta HTTP con la lista de notificaciones del stock en formato DTO o
     * un mensaje de error en caso de fallo.
     */

    @PostMapping("/getStockNotifications")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Object> getStockNotifications() {
        try {
            return new ResponseEntity<>(notificationService.getTopNotifications().stream().
                    map(NotificationMapper::notificationToNotificationDTO).collect(Collectors.toList()),
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Error al enviar la notificación"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
