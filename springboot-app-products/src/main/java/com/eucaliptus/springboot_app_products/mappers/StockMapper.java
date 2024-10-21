package com.eucaliptus.springboot_app_products.mappers;

import com.eucaliptus.springboot_app_products.dto.StockDTO;
import com.eucaliptus.springboot_app_products.model.Product;
import com.eucaliptus.springboot_app_products.model.ProductDetail;
import com.eucaliptus.springboot_app_products.model.Stock;

public class StockMapper {

    public static Stock stockDTOToStock(StockDTO stockDTO) {
        Stock stock = new Stock();
        stock.setIdStock(stockDTO.getIdStock());
        stock.setProduct(new Product());
        stock.setQuantityAvailable(stockDTO.getQuantityAvailable());
        stock.setProductDetail(new ProductDetail());
        return stock;
    }

    public static StockDTO stockToStockDTO(Stock stock) {
        StockDTO stockDTO = new StockDTO();
        stockDTO.setIdStock(stock.getIdStock());
        stockDTO.setIdProduct(stock.getProduct().getIdProduct());
        stockDTO.setQuantityAvailable(stock.getQuantityAvailable());
        stockDTO.setIdDetProduct(stock.getProductDetail().getIdDetProduct());
        return stockDTO;
    }
}
