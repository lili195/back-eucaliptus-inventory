package com.eucaliptus.springboot_app_billing.controllers;

import com.eucaliptus.springboot_app_billing.dto.DatesDTO;
import com.eucaliptus.springboot_app_billing.dto.SaleDTO;
import com.eucaliptus.springboot_app_billing.dto.SaleDetailDTO;
import com.eucaliptus.springboot_app_billing.mappers.SaleDetailMapper;
import com.eucaliptus.springboot_app_billing.mappers.SaleMapper;
import com.eucaliptus.springboot_app_billing.service.ProductService;
import com.eucaliptus.springboot_app_billing.service.SaleDetailService;
import com.eucaliptus.springboot_app_billing.service.SaleService;
import com.eucaliptus.springboot_app_products.dto.Message;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/billing/sale")
public class SaleController {
    @Autowired
    private SaleService saleService;
    @Autowired
    private SaleDetailService saleDetailService;
    @Autowired
    private ProductService productService;

    @PostMapping("/addSale")
    public ResponseEntity<Object> addSale(@RequestBody SaleDTO saleDTO, HttpServletRequest request) {
        try {
            SaleDTO sale = SaleMapper.saleToSaleDTO(saleService.addSale(saleDTO, request));
            List<SaleDetailDTO> saleDetailDTOS = saleDetailService.getSalesBySale(sale.getIdSale()).stream().
                    map(SaleDetailMapper::saleDetailToSaleDetailDTO).toList();
            saleDetailDTOS = productService.getSaleDetails(saleDetailDTOS, productService.getTokenByRequest(request));
            sale.setSaleDetails(saleDetailDTOS);
            return new ResponseEntity<>(sale, HttpStatus.OK);
        } catch (IllegalArgumentException e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/getProductsSale")
    public ResponseEntity<Object> getProductsSale(@RequestBody DatesDTO dates) {
        try {
            return new ResponseEntity<>(saleDetailService.getProductsSaleByRangeDate(dates.getStartDate(), dates.getEndDate()), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
