package com.eucaliptus.springboot_app_products.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
public class ReduceStockDTO {
    private String idProduct;
    private Date batch;
    private int idSaleDetail;
    private int quantitySold;
    private float salePrice;
    private float salePriceWithoutIva;
}
