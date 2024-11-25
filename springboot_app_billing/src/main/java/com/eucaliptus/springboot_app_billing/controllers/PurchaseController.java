package com.eucaliptus.springboot_app_billing.controllers;

import com.eucaliptus.springboot_app_billing.dto.DatesDTO;
import com.eucaliptus.springboot_app_billing.dto.Message;
import com.eucaliptus.springboot_app_billing.dto.PurchaseDTO;
import com.eucaliptus.springboot_app_billing.dto.PurchaseDetailDTO;
import com.eucaliptus.springboot_app_billing.mappers.PurchaseDetailMapper;
import com.eucaliptus.springboot_app_billing.mappers.PurchaseMapper;
import com.eucaliptus.springboot_app_billing.model.Purchase;
import com.eucaliptus.springboot_app_billing.security.JwtTokenUtil;
import com.eucaliptus.springboot_app_billing.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/billing/purchase")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    PurchaseDetailService purchaseDetailService;
    @Autowired
    private ProductService productService;
    @Autowired
    private APIService apiService;
    @Autowired
    private SendBillService sendBillService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/add")
    public ResponseEntity<Object> addPurchase(@RequestBody PurchaseDTO purchaseDTO, HttpServletRequest request) {
        try {
            PurchaseDTO purchase = PurchaseMapper.purchaseToPurchaseDTO(purchaseService.saveNewPurchase(purchaseDTO, request));
            List<PurchaseDetailDTO> purchaseDetailDTOS = purchaseDetailService.findByPurchaseId(purchase.getPurchaseId()).stream().
                    map(PurchaseDetailMapper::purchaseDetailToPurchaseDetailDTO).toList();
            purchaseDetailDTOS = productService.getPurchaseDetails(purchaseDetailDTOS, apiService.getTokenByRequest(request));
            purchase.setProviderDTO(purchaseService.getProvider(purchase.getProviderId(), apiService.getTokenByRequest(request)));
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
            return new ResponseEntity<>(purchases, HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getPurchaseDetails/{idPurchase}")
    public ResponseEntity<Object> getPurchaseDetails(@PathVariable int idPurchase, HttpServletRequest request) {
        try {
            Optional<Purchase> opPurchase = purchaseService.getById(idPurchase);
            if (opPurchase.isEmpty())
                return new ResponseEntity<>(new Message("Compra no encontrada"), HttpStatus.BAD_REQUEST);
            PurchaseDTO purchaseDTO = PurchaseMapper.purchaseToPurchaseDTO(opPurchase.get());
            List<PurchaseDetailDTO> purchaseDetailDTOS = purchaseDetailService.findByPurchaseId(purchaseDTO.getPurchaseId()).stream().
                    map(PurchaseDetailMapper::purchaseDetailToPurchaseDetailDTO).toList();
            purchaseDetailDTOS = productService.getPurchaseDetails(purchaseDetailDTOS, apiService.getTokenByRequest(request));
            purchaseDTO.setProviderDTO(purchaseService.getProvider(purchaseDTO.getProviderId(), apiService.getTokenByRequest(request)));
            purchaseDTO.setPurchaseDetails(purchaseDetailDTOS);
            return new ResponseEntity<>(purchaseDTO, HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/sendEmail/{idPurchase}")
    public ResponseEntity<Object> sendEmail(@PathVariable int idPurchase, HttpServletRequest request) {
        try {
            Optional<Purchase> opPurchase = purchaseService.getById(idPurchase);
            if (opPurchase.isEmpty())
                return new ResponseEntity<>(new Message("Compra no encontrada"), HttpStatus.BAD_REQUEST);
            PurchaseDTO purchaseDTO = PurchaseMapper.purchaseToPurchaseDTO(opPurchase.get());
            List<PurchaseDetailDTO> purchaseDetailDTOS = purchaseDetailService.findByPurchaseId(purchaseDTO.getPurchaseId()).stream().
                    map(PurchaseDetailMapper::purchaseDetailToPurchaseDetailDTO).toList();
            purchaseDetailDTOS = productService.getPurchaseDetails(purchaseDetailDTOS, apiService.getTokenByRequest(request));
            purchaseDTO.setProviderDTO(purchaseService.getProvider(purchaseDTO.getProviderId(), apiService.getTokenByRequest(request)));
            purchaseDTO.setPurchaseDetails(purchaseDetailDTOS);
            String email = jwtTokenUtil.extractAllClaims(apiService.getTokenByRequest(request)).get("email").toString();
            sendBillService.sendPurchase(email, purchaseDTO);
            return new ResponseEntity<>(purchaseDTO, HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
