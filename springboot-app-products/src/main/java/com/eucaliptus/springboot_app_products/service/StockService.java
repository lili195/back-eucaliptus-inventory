package com.eucaliptus.springboot_app_products.service;

import com.eucaliptus.springboot_app_products.model.Stock;
import com.eucaliptus.springboot_app_products.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public Optional<Stock> getStockById(Long idStock) {
        return stockRepository.findById(idStock);
    }

    public Optional<Stock> getStockByProductId(String productId) {
        return stockRepository.findByProduct_IdProduct(productId);
    }

    public Stock saveStock(Stock stock) {
        return stockRepository.save(stock);
    }

    public Stock updateStock(Long id, Stock stockDetails) {
        Optional<Stock> existingStock = stockRepository.findById(id);

        if (existingStock.isPresent()) {
            Stock stockToUpdate = existingStock.get();
            stockToUpdate.setQuantityAvailable(stockDetails.getQuantityAvailable()); // Actualiza los campos que necesites
            stockToUpdate.setProduct(stockDetails.getProduct());   // Otros campos...

            return stockRepository.save(stockToUpdate);
        } else {
            throw new RuntimeException("Stock no encontrado");
        }
    }


    public boolean existsByIdStock(Long idStock) {
        return stockRepository.existsById(idStock);
    }

    public boolean existsById(Long id) {
        return stockRepository.existsById(id); // Asume que tienes un repositorio de stock
    }


    public boolean deleteStock(Long idStock) {
        return stockRepository.findById(idStock).map(stock -> {
            stockRepository.delete(stock);
            return true;
        }).orElse(false);
    }
}
