package com.eucaliptus.springboot_app_products.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
public class StockDTO {
    private Long idStock;
    private ProductDTO productDTO;
    private Long idSaleDetail;
    private Long idPurchaseDetail;
    private Integer quantityAvailable;
    private Double productSalePrice;
    private Double productSalePriceWithoutIVA;
    private int iva;
    private Date modificationDate;
}

