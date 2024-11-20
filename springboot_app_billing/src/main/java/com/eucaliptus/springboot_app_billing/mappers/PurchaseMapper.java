package com.eucaliptus.springboot_app_billing.mappers;

import com.eucaliptus.springboot_app_billing.dto.PurchaseDTO;
import com.eucaliptus.springboot_app_billing.model.Purchase;

public class PurchaseMapper {

    public static Purchase purchaseDTOToPurchase(PurchaseDTO purchaseDTO) {
        Purchase purchase = new Purchase();
        purchase.setIdProvider(purchaseDTO.getProviderId());
        purchase.setPurchaseDate(purchaseDTO.getPurchaseDate());
        purchase.setTotalPurchase(purchaseDTO.getTotalPurchase());
        return purchase;
    }

    public static PurchaseDTO purchaseToPurchaseDTO(Purchase purchase) {
        PurchaseDTO purchaseDTO = new PurchaseDTO();
        purchaseDTO.setPurchaseId(purchase.getIdPurchase());
        purchaseDTO.setProviderId(purchase.getIdProvider());
        purchaseDTO.setPurchaseDate(purchase.getPurchaseDate());
        purchaseDTO.setTotalPurchase(purchase.getTotalPurchase());
        return purchaseDTO;
    }
}
