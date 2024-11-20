package com.eucaliptus.springboot_app_billing.controllers;

import com.eucaliptus.springboot_app_billing.dto.DatesDTO;
import com.eucaliptus.springboot_app_billing.dto.PurchaseDTO;
import com.eucaliptus.springboot_app_billing.dto.PurchaseDetailDTO;
import com.eucaliptus.springboot_app_billing.mappers.PurchaseDetailMapper;
import com.eucaliptus.springboot_app_billing.mappers.PurchaseMapper;
import com.eucaliptus.springboot_app_billing.service.ProductService;
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

import java.util.List;

@RestController
@RequestMapping("/billing/purchase")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    PurchaseDetailService purchaseDetailService;
    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<Object> addPurchase(@RequestBody PurchaseDTO purchaseDTO, HttpServletRequest request) {
        try {
            PurchaseDTO purchase = PurchaseMapper.purchaseToPurchaseDTO(purchaseService.saveNewPurchase(purchaseDTO, request));
            List<PurchaseDetailDTO> purchaseDetailDTOS = purchaseDetailService.findByPurchaseId(purchase.getPurchaseId()).stream().
                    map(PurchaseDetailMapper::purchaseDetailToPurchaseDetailDTO).toList();
            purchaseDetailDTOS = productService.getPurchaseDetails(purchaseDetailDTOS, productService.getTokenByRequest(request));
            purchase.setPurchaseDetails(purchaseDetailDTOS);
            return new ResponseEntity<>(purchase, HttpStatus.OK);
        } catch (IllegalArgumentException e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/getHistoryPurchase")
    public ResponseEntity<Object> getHistoryPurchase(@RequestBody DatesDTO date, HttpServletRequest request) {
        try {
            List<PurchaseDTO> purchases = purchaseService.getPurchasesByDate(date.getStartDate()).stream().map(PurchaseMapper::purchaseToPurchaseDTO).toList();
            for (PurchaseDTO purchaseDTO : purchases) {
                purchaseDTO.setPurchaseDetails(purchaseDetailService.findByPurchaseId(purchaseDTO.getPurchaseId()).stream().
                        map(PurchaseDetailMapper::purchaseDetailToPurchaseDetailDTO).toList());
            }
            purchases = productService.getProductsFromPurchase(purchases, productService.getTokenByRequest(request));
            return new ResponseEntity<>(purchases, HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
