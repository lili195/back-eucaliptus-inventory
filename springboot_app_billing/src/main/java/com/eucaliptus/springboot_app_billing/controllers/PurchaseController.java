package com.eucaliptus.springboot_app_billing.controllers;

import com.eucaliptus.springboot_app_billing.dto.PurchaseDTO;
import com.eucaliptus.springboot_app_billing.mappers.PurchaseMapper;
import com.eucaliptus.springboot_app_billing.model.Purchase;
import com.eucaliptus.springboot_app_billing.service.PurchaseDetailService;
import com.eucaliptus.springboot_app_billing.service.PurchaseService;
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
@RequestMapping("/billing/purchase")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @PostMapping("/add")
    public ResponseEntity<Object> addPurchase(@RequestBody PurchaseDTO purchaseDTO, HttpServletRequest request) {
        try {
            if (!purchaseService.saveNewPurchase(purchaseDTO, request))
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
