package com.eucaliptus.springboot_app_billing.mappers;


import com.eucaliptus.springboot_app_billing.dto.SaleDetailDTO;
import com.eucaliptus.springboot_app_billing.model.SaleDetail;
import com.eucaliptus.springboot_app_products.dto.RequestBatchDTO;

public class SaleDetailMapper {
    public static SaleDetail saleDetailDTOToSaleDetail(SaleDetailDTO saleDetailDTO) {
        SaleDetail saleDetail = new SaleDetail();
        saleDetail.setIdProduct(saleDetailDTO.getIdProduct());
        saleDetail.setBatch(saleDetailDTO.getBatch());
        saleDetail.setQuantitySold(saleDetailDTO.getQuantitySold());
        saleDetail.setSalePrice(saleDetailDTO.getSalePrice());
        saleDetail.setSalePriceWithoutIva(saleDetailDTO.getSalePriceWithoutIva());
        return saleDetail;
    }

    public static RequestBatchDTO saleDetailDTOToRequestBatchDTO(SaleDetailDTO saleDetailDTO) {
        RequestBatchDTO requestBatchDTO = new RequestBatchDTO();
        requestBatchDTO.setIdProduct(saleDetailDTO.getIdProduct());
        requestBatchDTO.setQuantity(saleDetailDTO.getQuantitySold());
        return requestBatchDTO;
    }

    public static SaleDetailDTO saleDetailToSaleDetailDTO(SaleDetail saleDetail) {
        SaleDetailDTO saleDetailDTO = new SaleDetailDTO();
        saleDetailDTO.setIdSaleDetail(saleDetail.getIdSaleDetail());
        saleDetailDTO.setIdProduct(saleDetail.getIdProduct());
        saleDetailDTO.setBatch(saleDetail.getBatch());
        saleDetailDTO.setQuantitySold(saleDetail.getQuantitySold());
        saleDetailDTO.setSalePrice(saleDetail.getSalePrice());
        saleDetailDTO.setSalePriceWithoutIva(saleDetail.getSalePriceWithoutIva());
        saleDetailDTO.setIva(calculateIva(saleDetail.getSalePrice(), saleDetail.getSalePriceWithoutIva()));
        return saleDetailDTO;
    }

    private static Integer calculateIva(Double priceWithIva, Double priceWithoutIva) {
        return (int) Math.round((priceWithIva - priceWithoutIva) / priceWithoutIva * 100);
    }

}
