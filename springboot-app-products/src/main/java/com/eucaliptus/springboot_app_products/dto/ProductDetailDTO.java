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
    private Integer quantity;
    private Double unitPrice;
    private Date batch;
    private Date dueDate;
}
