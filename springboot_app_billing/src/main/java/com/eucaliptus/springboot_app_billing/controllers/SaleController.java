package com.eucaliptus.springboot_app_billing.controllers;

import com.eucaliptus.springboot_app_billing.dto.DatesDTO;
import com.eucaliptus.springboot_app_billing.dto.SaleDTO;
import com.eucaliptus.springboot_app_billing.dto.SaleDetailDTO;
import com.eucaliptus.springboot_app_billing.mappers.SaleDetailMapper;
import com.eucaliptus.springboot_app_billing.mappers.SaleMapper;
import com.eucaliptus.springboot_app_billing.model.Sale;
import com.eucaliptus.springboot_app_billing.model.Summary;
import com.eucaliptus.springboot_app_billing.service.*;
import com.eucaliptus.springboot_app_products.dto.Message;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/billing/sale")
public class SaleController {
    @Autowired
    private SaleService saleService;
    @Autowired
    private SaleDetailService saleDetailService;
    @Autowired
    private ProductService productService;
    @Autowired
    private SummaryService summaryService;
    @Autowired
    private APIService apiService;

    @PostMapping("/addSale")
    public ResponseEntity<Object> addSale(@RequestBody SaleDTO saleDTO, HttpServletRequest request) {
        try {
            SaleDTO sale = SaleMapper.saleToSaleDTO(saleService.addSale(saleDTO, request));
            List<SaleDetailDTO> saleDetailDTOS = saleDetailService.getSalesBySale(sale.getIdSale()).stream().
                    map(SaleDetailMapper::saleDetailToSaleDetailDTO).toList();
            saleDetailDTOS = productService.getSaleDetails(saleDetailDTOS, apiService.getTokenByRequest(request));
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

    @PostMapping("/getHistorySale")
    public ResponseEntity<Object> getHistorySale(@RequestBody DatesDTO date, HttpServletRequest request) {
        try {
            List<SaleDTO> sales = saleService.getSalesByDate(date.getStartDate()).stream().map(SaleMapper::saleToSaleDTO).toList();
            return new ResponseEntity<>(sales, HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getSaleDetails/{idSale}")
    public ResponseEntity<Object> getSaleDetails(@PathVariable int idSale, HttpServletRequest request) {
        try {
            Optional<Sale> opSale= saleService.getSaleById(idSale);
            if (opSale.isEmpty())
                return new ResponseEntity<>(new Message("Venta no encontrada"), HttpStatus.BAD_REQUEST);
            SaleDTO saleDTO = SaleMapper.saleToSaleDTO(opSale.get());
            List<SaleDetailDTO> saleDetailDTOS = saleDetailService.getSalesBySale(saleDTO.getIdSale()).stream().
                    map(SaleDetailMapper::saleDetailToSaleDetailDTO).toList();
            saleDetailDTOS = productService.getSaleDetails(saleDetailDTOS, apiService.getTokenByRequest(request));
            saleDTO.setSaleDetails(saleDetailDTOS);
            return new ResponseEntity<>(saleDTO, HttpStatus.OK);
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

    @GetMapping("/summary")
    public ResponseEntity<Object> getGlobalSummary() {
        try {
            Optional<Summary> summary = summaryService.getGlobalSummary();
            if (summary.isEmpty()) {
                return new ResponseEntity<>(new Message("Resumen no disponible"), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(summary.get(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Error al obtener el resumen global"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
