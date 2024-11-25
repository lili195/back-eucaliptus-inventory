package com.eucaliptus.springboot_app_billing.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RequestBatchDTO {
    private String idProduct;
    private int quantity;
}
