package com.eucaliptus.springboot_app_products.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ProductDTO {
    private Long idProduct;
    private String productName;
    private String brand;
    private String category;
    private String use;
    private Long idProvider;
    private String description;
    private Long idUnit;
    private Integer minimumProductAmount;
    private Integer maximumProductAmount;
}
