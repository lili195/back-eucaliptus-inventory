package com.eucaliptus.springboot_app_products.controllers;

import com.eucaliptus.springboot_app_products.dto.StockDTO;
import com.eucaliptus.springboot_app_products.mappers.StockMapper;
import com.eucaliptus.springboot_app_products.model.Stock;
import com.eucaliptus.springboot_app_products.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/inventory/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllStocks() {
        try {
            List<StockDTO> stocks = stockService.getAllStocks().stream()
                    .map(StockMapper::stockToStockDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(stocks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Intente de nuevo más tarde", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getStockById/{id}")
    public ResponseEntity<Object> getStockById(@PathVariable Long id) {
        try {
            if (!stockService.existsById(id)) {
                return new ResponseEntity<>("Stock no encontrado", HttpStatus.BAD_REQUEST);
            }
            Optional<Stock> stock = stockService.getStockById(id);

            // Asegúrate de que el Optional tiene un valor antes de usarlo
            if (stock.isPresent()) {
                return new ResponseEntity<>(StockMapper.stockToStockDTO(stock.get()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Stock no encontrado", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Inténtalo más tarde", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/addStock")
    public ResponseEntity<Object> createStock(@RequestBody StockDTO stockDTO) {
        try {
            Stock stock = StockMapper.stockDTOToStock(stockDTO);
            Stock savedStock = stockService.saveStock(stock);
            return new ResponseEntity<>(StockMapper.stockToStockDTO(savedStock), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Intente de nuevo más tarde", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateStock/{id}")
    public ResponseEntity<Object> updateStock(@PathVariable("id") Long idStock, @RequestBody StockDTO stockDetails) {
        try {
            if (!stockService.existsById(idStock)) {
                return new ResponseEntity<>("Este stock no existe", HttpStatus.BAD_REQUEST);
            }
            Stock stock = StockMapper.stockDTOToStock(stockDetails);
            Stock updatedStock = stockService.updateStock(idStock, stock);
            return new ResponseEntity<>(StockMapper.stockToStockDTO(updatedStock), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Intente de nuevo más tarde", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/deleteStock/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        return stockService.deleteStock(id) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
}
