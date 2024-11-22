package com.eucaliptus.springboot_app_products.controllers;

import com.eucaliptus.springboot_app_products.dto.*;
import com.eucaliptus.springboot_app_products.mappers.StockMapper;
import com.eucaliptus.springboot_app_products.model.Stock;
import com.eucaliptus.springboot_app_products.service.BatchService;
import com.eucaliptus.springboot_app_products.service.NotificationService;
import com.eucaliptus.springboot_app_products.service.SaleService;
import com.eucaliptus.springboot_app_products.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products/stock")
public class StockController {

    @Autowired
    private StockService stockService;
    @Autowired
    private BatchService batchService;
    @Autowired
    private SaleService saleService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Object> findAll() {
        try{
            return new ResponseEntity<>(stockService.getStocksAvailable().stream().
                    map(StockMapper::stockToStockDTO).collect(Collectors.toList()), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo más tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/validatePurchase")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Object> validatePurchase(@RequestBody List<NewBatchDTO> batches) {
        return batchService.validatePurchase(batches);
    }

    @PostMapping("/addBatches")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Object> addBatches(@RequestBody List<NewBatchDTO> batches) {
        try {
            if (!batchService.addBatches(batches))
                return new ResponseEntity<>(new Message("Error al agregar el nuevo lote"), HttpStatus.BAD_REQUEST);
            if (!stockService.updateStocks(batches))
                return new ResponseEntity<>(new Message("Error al actualizar el stock"), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(new Message("Lotes agregados y stock actualizado"), HttpStatus.OK);
        } catch (IllegalArgumentException e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo más tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/requestBatches")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Object> requestBatches(@RequestBody List<RequestBatchDTO> requests) {
        try {
            return new ResponseEntity<>(saleService.getSaleDetails(requests), HttpStatus.OK);
        } catch (IllegalArgumentException e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo más tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/reduceStock")
    public ResponseEntity<Object> reduceStock(@RequestBody List<SaleDetailDTO> saleDetails) {
        try {
            if(!saleService.updateStock(saleDetails))
                return new ResponseEntity<>(new Message("Error al actualizar el stock"), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(new Message("Lotes y stock actualizados"), HttpStatus.OK);
        } catch (IllegalArgumentException e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo más tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/sendStockNotification")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Object> sendStockNotification(@RequestBody Stock stock) {
        try {
            // Generar la notificación
            NotificationDTO notification = new NotificationDTO();
            notification.setIdNotification("unique-id-" + System.currentTimeMillis()); // Genera un ID único
            notification.setMessage("Stock actualizado para el producto " + stock.getProduct().getIdProduct());
            notification.setNotificationDate(new Date());
            notification.setIdStock(Math.toIntExact(stock.getIdStock()));
            notification.setIdProduct(Integer.valueOf(stock.getProduct().getIdProduct()));

            // Llamada al microservicio de notificaciones
            notificationService.sendNotification(notification);
            return new ResponseEntity<>(new Message("Notificación enviada con éxito"), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Error al enviar la notificación"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
