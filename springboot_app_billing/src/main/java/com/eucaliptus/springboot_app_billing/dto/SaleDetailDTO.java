package com.eucaliptus.springboot_app_billing.dto;

import com.eucaliptus.springboot_app_products.dto.ProductDTO;
import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaleDetailDTO {
    private Integer idSaleDetail;
    private String idProduct;
    private ProductDTO productDTO;
    private Date batch;
    private Integer quantitySold;
    private Double salePrice;
    private Double salePriceWithoutIva;
    private int iva;
}
