package com.eucaliptus.springboot_app_billing.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaleDTO {
    private Integer idSale;
    private String idSeller;
    private ClientDTO clientDTO;
    private Date dateSale;
    private Double total;
    private List<SaleDetailDTO> saleDetails;
}
