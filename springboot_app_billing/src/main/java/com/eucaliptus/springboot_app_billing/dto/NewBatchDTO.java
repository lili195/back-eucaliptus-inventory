package com.eucaliptus.springboot_app_billing.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
public class NewBatchDTO {
    private String idProduct;
    private Long idPurchaseDetail;
    private Date batchPurchase;
    private int quantityPurchased;
    private Double salePrice;
    private Double salePriceWithoutIva;
    private Date purchaseDueDate;
}
