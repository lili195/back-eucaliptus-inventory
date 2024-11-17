package com.eucaliptus.springboot_app_billing.controllers;

import com.eucaliptus.springboot_app_billing.dto.SaleDTO;
import com.eucaliptus.springboot_app_billing.service.SaleDetailService;
import com.eucaliptus.springboot_app_billing.service.SaleService;
import com.eucaliptus.springboot_app_products.dto.Message;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/billing/sale")
public class SaleController {
    @Autowired
    private SaleService saleService;

    @PostMapping("/addSale")
    public ResponseEntity<Object> addSale(@RequestBody SaleDTO saleDTO, HttpServletRequest request) {
        try {
            if (!saleService.addSale(saleDTO, request))
                return new ResponseEntity<>(new Message("No se pudo guardar la compra"), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(new Message("Se ha guardado la compra"), HttpStatus.OK);
        } catch (IllegalArgumentException e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
