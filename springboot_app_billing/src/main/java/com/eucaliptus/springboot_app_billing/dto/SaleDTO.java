package com.eucaliptus.springboot_app_billing.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class SaleDTO {
    private Integer idSale;
    private String idProduct;  // Llave foránea de otro microservicio
    private Integer quantity;
    private Double unitPrice;
    private Integer idBill;

}
