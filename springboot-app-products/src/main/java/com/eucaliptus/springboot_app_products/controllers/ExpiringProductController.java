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

@RestController
@RequestMapping("/api/products")
public class ExpiringProductController {

    private final ExpiringProductService service;

    public ExpiringProductController(ExpiringProductService service) {
        this.service = service;
    }

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