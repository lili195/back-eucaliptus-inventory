package com.eucaliptus.springboot_app_products.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
public class ProductDetailDTO {
    private Long idDetProduct;
    private ProductDTO productDTO;
    private Integer quantity;
    private Double inputUnitPrice;
    private Double inputUnitPriceWithoutIVA;
    private Double outputUnitPrice;
    private Double outputUnitPriceWithoutIVA;
    private Integer iva;
    private Date batch;
    private Date dueDate;
}
