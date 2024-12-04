package com.eucaliptus.springboot_app_products.controllers;

import com.eucaliptus.springboot_app_products.dto.Message;
import com.eucaliptus.springboot_app_products.model.ExpiringProduct;
import com.eucaliptus.springboot_app_products.repository.ExpiringProductRepository;
import com.eucaliptus.springboot_app_products.model.ExpiringProduct;
import com.eucaliptus.springboot_app_products.service.ExpiringProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para gestionar productos con fechas de vencimiento.
 *
 * <p>Este controlador proporciona endpoints para acceder a los productos
 * que están próximos a vencer. Solo usuarios con roles específicos
 * tienen acceso a estas funcionalidades.</p>
 */


@RestController
@RequestMapping("/api/products")
public class ExpiringProductController {

    /**
     * Constructor para inyectar el servicio de productos próximos a vencer.
     *
     * @param service el servicio utilizado para manejar la lógica de negocio
     *                relacionada con los productos próximos a vencer.
     */

    private final ExpiringProductService service;

    public ExpiringProductController(ExpiringProductService service) {
        this.service = service;
    }

    /**
     * Endpoint para obtener una lista de productos con fechas próximas a vencer.
     *
     * <p>Este método está protegido por autorización, permitiendo acceso solo
     * a usuarios con los roles <code>ROLE_ADMIN</code> o <code>ROLE_SELLER</code>.</p>
     *
     * @return una respuesta HTTP con la lista de productos próximos a vencer o
     * un mensaje de error en caso de fallo.
     */

    @GetMapping("/expiring")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Object> getExpiringProducts() {
        try {
            return ResponseEntity.ok(
                    service.getExpiringProducts().stream()
                            .collect(Collectors.toList()) // Opcional si deseas realizar más transformaciones
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Message("Intente de nuevo más tarde"));
        }
    }
}