package com.eucaliptus.springboot_app_products.service;

import com.eucaliptus.springboot_app_products.dto.NewBatchDTO;
import com.eucaliptus.springboot_app_products.dto.NotificationDTO;
import com.eucaliptus.springboot_app_products.mappers.StockMapper;
import com.eucaliptus.springboot_app_products.model.Product;
import com.eucaliptus.springboot_app_products.model.Stock;
import com.eucaliptus.springboot_app_products.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private ProductService productService;

    @Autowired
    private NotificationService notificationService;

    public List<Stock> getStocksAvailable(){
        return stockRepository.findLatestStockByProduct();
    }

    public Optional<Stock> getStockByProductId(String productId) {
        return stockRepository.findFirstByProduct_IdProductOrderByModificationDateStockDesc(productId);
    }

    public Stock saveStock(Stock stock) {
        return stockRepository.save(stock);
    }

    @Transactional
    public boolean updateStocks(List<NewBatchDTO> batches){
        for (NewBatchDTO batch : batches) {
            Optional<Product> opProduct = productService.getProductById(batch.getIdProduct());
            if (opProduct.isEmpty())
                throw new IllegalArgumentException("Producto no encontrado: " + batch.getIdProduct());
            Stock stock = StockMapper.newBatchDTOToStock(batch);
            stock.setProduct(opProduct.get());
            Optional<Stock> existStock = getStockByProductId(batch.getIdProduct());
            int quantity = (existStock.isEmpty()) ?
                    batch.getQuantityPurchased():
                    existStock.get().getQuantityAvailable() + batch.getQuantityPurchased();
            stock.setQuantityAvailable(quantity);
            saveStock(stock);
        }
        return true;
    }
}
