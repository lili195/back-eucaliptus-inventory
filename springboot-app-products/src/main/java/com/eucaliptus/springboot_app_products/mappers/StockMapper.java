package com.eucaliptus.springboot_app_products.mappers;

import com.eucaliptus.springboot_app_person.mappers.ProviderMapper;
import com.eucaliptus.springboot_app_products.dto.StockDTO;
import com.eucaliptus.springboot_app_products.model.Product;
import com.eucaliptus.springboot_app_products.model.ProductDetail;
import com.eucaliptus.springboot_app_products.model.Stock;

public class StockMapper {

    public static Stock stockDTOToStock(StockDTO stockDTO) {
        Stock stock = new Stock();
        stock.setIdStock(stockDTO.getIdStock());
        stock.setQuantityAvailable(stockDTO.getQuantityAvailable());
        return stock;
    }

    public static StockDTO stockToStockDTO(Stock stock) {
        StockDTO stockDTO = new StockDTO();
        stockDTO.setIdStock(stock.getIdStock());
        stockDTO.setProductDTO(ProductMapper.productToProductDTO(stock.getProduct()));
        stockDTO.setQuantityAvailable(stock.getQuantityAvailable());
        return stockDTO;
    }
}
