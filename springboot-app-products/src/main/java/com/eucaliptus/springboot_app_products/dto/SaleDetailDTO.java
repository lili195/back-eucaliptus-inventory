package com.eucaliptus.springboot_app_products.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
public class SaleDetailDTO {
    private Long idSaleDetail;
    private Long idSale;
    private String idProduct;
    private ProductDTO productDTO;
    private Date batch;
    private int quantitySold;
    private Double salePrice;
    private int iva;
    private Double salePriceWithoutIva;
}
