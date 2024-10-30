package com.eucaliptus.springboot_app_products.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class StockDTO {
    private Long idStock;
    private ProductDTO productDTO;
    private Integer quantityAvailable;
}

