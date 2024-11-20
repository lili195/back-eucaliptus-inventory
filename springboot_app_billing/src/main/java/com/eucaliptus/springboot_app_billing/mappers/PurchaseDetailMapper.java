package com.eucaliptus.springboot_app_billing.mappers;

import com.eucaliptus.springboot_app_billing.dto.PurchaseDetailDTO;
import com.eucaliptus.springboot_app_billing.model.PurchaseDetail;
import com.eucaliptus.springboot_app_products.dto.NewBatchDTO;

public class PurchaseDetailMapper {

    public static PurchaseDetail purchaseDetailDTOToPurchaseDetail(PurchaseDetailDTO purchaseDetailDTO) {
        PurchaseDetail purchaseDetail = new PurchaseDetail();
        purchaseDetail.setIdProduct(purchaseDetailDTO.getIdProduct());
        purchaseDetail.setBatchPurchase(purchaseDetailDTO.getBatchPurchase());
        purchaseDetail.setQuantityPurchased(purchaseDetailDTO.getQuantityPurchased());
        purchaseDetail.setPurchasePrice(purchaseDetailDTO.getPurchasePrice());
        purchaseDetail.setPurchasePriceWithoutIva(getPriceWithoutIva(purchaseDetailDTO.getPurchasePrice(), purchaseDetailDTO.getIva()));
        purchaseDetail.setSalePrice(purchaseDetailDTO.getSalePrice());
        purchaseDetail.setSalePriceWithoutIva(getPriceWithoutIva(purchaseDetailDTO.getSalePrice(), purchaseDetailDTO.getIva()));
        purchaseDetail.setPurchaseDueDate(purchaseDetailDTO.getPurchaseDueDate());
        return purchaseDetail;
    }

    public static NewBatchDTO purchaseDetailToNewBatchDTO(PurchaseDetail purchaseDetail) {
        NewBatchDTO newBatchDTO = new NewBatchDTO();
        newBatchDTO.setIdProduct(purchaseDetail.getIdProduct());
        newBatchDTO.setIdPurchaseDetail((long)purchaseDetail.getIdPurchaseDetail());
        newBatchDTO.setBatchPurchase(purchaseDetail.getBatchPurchase());
        newBatchDTO.setQuantityPurchased(purchaseDetail.getQuantityPurchased());
        newBatchDTO.setSalePrice(purchaseDetail.getSalePrice());
        newBatchDTO.setSalePriceWithoutIva(purchaseDetail.getSalePriceWithoutIva());
        newBatchDTO.setPurchaseDueDate(purchaseDetail.getPurchaseDueDate());
        return newBatchDTO;
    }

    public static PurchaseDetailDTO purchaseDetailToPurchaseDetailDTO(PurchaseDetail purchaseDetail) {
        PurchaseDetailDTO purchaseDetailDTO = new PurchaseDetailDTO();
        purchaseDetailDTO.setIdProduct(purchaseDetail.getIdProduct());
        purchaseDetailDTO.setBatchPurchase(purchaseDetail.getBatchPurchase());
        purchaseDetailDTO.setQuantityPurchased(purchaseDetail.getQuantityPurchased());
        purchaseDetailDTO.setPurchasePrice(purchaseDetail.getPurchasePrice());
        purchaseDetailDTO.setPurchasePriceWithoutIva(purchaseDetail.getPurchasePriceWithoutIva());
        purchaseDetailDTO.setSalePrice(purchaseDetail.getSalePrice());
        purchaseDetailDTO.setSalePriceWithoutIva(purchaseDetail.getSalePriceWithoutIva());
        purchaseDetailDTO.setIva(calculateIva(purchaseDetail.getPurchasePrice(), purchaseDetail.getPurchasePriceWithoutIva()));
        purchaseDetailDTO.setPurchaseDueDate(purchaseDetail.getPurchaseDueDate());
        return purchaseDetailDTO;
    }

    private static Double getPriceWithoutIva(Double price, Integer iva) {
        return price / (1.0 + ((double)iva / 100));
    }

    private static Integer calculateIva(Double priceWithIva, Double priceWithoutIva) {
        return (int) Math.round((priceWithIva - priceWithoutIva) / priceWithoutIva * 100);
    }
}
