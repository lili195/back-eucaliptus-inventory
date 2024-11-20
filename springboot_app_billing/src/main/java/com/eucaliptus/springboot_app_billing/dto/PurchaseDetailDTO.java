package com.eucaliptus.springboot_app_billing.dto;

import com.eucaliptus.springboot_app_products.dto.ProductDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
public class PurchaseDetailDTO {
    private int idPurchaseDetail;
    private String idProduct;
    private ProductDTO productDTO;
    private Date batchPurchase;
    private int quantityPurchased;
    private Double purchasePrice;
    private Double purchasePriceWithoutIva;
    private Double salePrice;
    private Double salePriceWithoutIva;
    private int iva;
    private Date purchaseDueDate;
}
