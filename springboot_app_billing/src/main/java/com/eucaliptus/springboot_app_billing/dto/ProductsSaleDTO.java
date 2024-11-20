package com.eucaliptus.springboot_app_billing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class ProductsSaleDTO {
    private String productId;
    private Long quantity;
    private Long totalPrice;
}
