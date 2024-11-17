package com.eucaliptus.springboot_app_billing.mappers;

import com.eucaliptus.springboot_app_billing.dto.SaleDTO;
import com.eucaliptus.springboot_app_billing.model.Sale;

public class SaleMapper {
    public static Sale saleDTOToSale(SaleDTO saleDTO) {
        Sale sale = new Sale();
        sale.setSaleDate(saleDTO.getDateSale());
        sale.setTotalSale(saleDTO.getTotal());
        return sale;
    }

    public static SaleDTO saleToSaleDTO(Sale sale) {
        SaleDTO saleDTO = new SaleDTO();
        saleDTO.setIdSale(sale.getIdSale());
        saleDTO.setDateSale(sale.getSaleDate());
        saleDTO.setTotal(sale.getTotalSale());
        saleDTO.setIdSeller(sale.getIdSeller());
        saleDTO.setClientDTO(ClientMapper.clientToClientDTO(sale.getClient()));
        return saleDTO;
    }
}
