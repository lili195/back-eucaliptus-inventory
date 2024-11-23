package com.eucaliptus.springboot_app_billing.dto;

import com.eucaliptus.springboot_app_person.dtos.ProviderDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
public class PurchaseDTO {
    private int purchaseId;
    private String providerId;
    private ProviderDTO providerDTO;
    private Date purchaseDate;
    private Double totalPurchase;
    private List<PurchaseDetailDTO> purchaseDetails;
}
