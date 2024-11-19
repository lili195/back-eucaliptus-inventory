package com.eucaliptus.springboot_app_products.mappers;

import com.eucaliptus.springboot_app_products.dto.NewBatchDTO;
import com.eucaliptus.springboot_app_products.dto.SaleDetailDTO;
import com.eucaliptus.springboot_app_products.dto.StockDTO;
import com.eucaliptus.springboot_app_products.model.Stock;

import java.util.Date;

public class StockMapper {

    public static Stock stockDTOToStock(StockDTO stockDTO) {
        Stock stock = new Stock();
        stock.setIdStock(stockDTO.getIdStock());
        stock.setIdPurchaseDetail(stockDTO.getIdPurchaseDetail());
        stock.setIdSaleDetail(stockDTO.getIdSaleDetail());
        stock.setQuantityAvailable(stockDTO.getQuantityAvailable());
        stock.setProductSalePrice(stockDTO.getProductSalePrice());
        stock.setProductSalePriceWithoutIva(stock.getProductSalePriceWithoutIva());
        stock.setModificationDateStock(stock.getModificationDateStock());
        return stock;
    }

    public static Stock newBatchDTOToStock(NewBatchDTO newBatchDTO) {
        Stock stock = new Stock();
        stock.setIdPurchaseDetail(newBatchDTO.getIdPurchaseDetail());
        stock.setProductSalePrice(newBatchDTO.getSalePrice());
        stock.setProductSalePriceWithoutIva(newBatchDTO.getSalePriceWithoutIva());
        stock.setModificationDateStock(new Date());
        return stock;
    }

    public static Stock SaleDetailDTOToStock(SaleDetailDTO saleDetailDTO) {
        Stock stock = new Stock();
        stock.setIdSaleDetail(saleDetailDTO.getIdSaleDetail());
        stock.setProductSalePrice(saleDetailDTO.getSalePrice());
        stock.setProductSalePriceWithoutIva(saleDetailDTO.getSalePriceWithoutIva());
        stock.setModificationDateStock(new Date());
        return stock;
    }

    public static StockDTO stockToStockDTO(Stock stock) {
        StockDTO stockDTO = new StockDTO();
        stockDTO.setProductDTO(ProductMapper.productToProductDTO(stock.getProduct()));
        stockDTO.setQuantityAvailable(stock.getQuantityAvailable());
        stockDTO.setProductSalePrice(stock.getProductSalePrice());
        stockDTO.setProductSalePriceWithoutIVA(stock.getProductSalePriceWithoutIva());
        stockDTO.setIva(calculateIva(stock.getProductSalePrice(), stock.getProductSalePriceWithoutIva()));
        return stockDTO;
    }

    private static Integer calculateIva(Double priceWithIva, Double priceWithoutIva) {
        return (int) Math.round((priceWithIva - priceWithoutIva) / priceWithoutIva * 100);
    }


}
