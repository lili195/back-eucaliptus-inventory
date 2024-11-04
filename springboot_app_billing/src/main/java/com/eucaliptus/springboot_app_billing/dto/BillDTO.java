package com.eucaliptus.springboot_app_billing.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BillDTO {

    private Integer idBill;
    private Date billDate;
    private Double total;
    private Integer idPerson;  // Llave foránea de otro microservicio
    private Integer idClient;
    private List<SaleDTO> sales;
}
