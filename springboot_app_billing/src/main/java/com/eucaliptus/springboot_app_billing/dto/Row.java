package com.eucaliptus.springboot_app_billing.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Row {
    private ProductDTO productDTO;
    private String unitPrice;
    private int quantity;
    private String subtotal;
}
