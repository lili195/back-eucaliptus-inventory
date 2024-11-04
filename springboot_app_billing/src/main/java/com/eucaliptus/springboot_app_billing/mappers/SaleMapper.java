package com.eucaliptus.springboot_app_billing.mappers;


import com.eucaliptus.springboot_app_billing.dto.SaleDTO;
import com.eucaliptus.springboot_app_billing.model.Sale;

public class SaleMapper {
    public static Sale saleDTOToSale(SaleDTO saleDTO) {
        Sale sale = new Sale();
        sale.setIdSale(saleDTO.getIdSale());
        sale.setIdProduct(saleDTO.getIdProduct());
        sale.setQuantity(saleDTO.getQuantity());
        sale.setUnitPrice(saleDTO.getUnitPrice());
        return sale;
    }

    public static SaleDTO saleToSaleDTO(Sale sale) {
        SaleDTO saleDTO = new SaleDTO();
        saleDTO.setIdSale(sale.getIdSale());
        saleDTO.setIdProduct(sale.getIdProduct());
        saleDTO.setQuantity(sale.getQuantity());
        saleDTO.setUnitPrice(sale.getUnitPrice());
        saleDTO.setIdBill(sale.getBill().getIdBill()); // Asumiendo que Bill no es nulo
        return saleDTO;
    }

}
