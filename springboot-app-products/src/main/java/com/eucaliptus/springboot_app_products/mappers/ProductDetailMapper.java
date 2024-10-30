package com.eucaliptus.springboot_app_products.mappers;

import com.eucaliptus.springboot_app_products.dto.ProductDetailDTO;
import com.eucaliptus.springboot_app_products.model.ProductDetail;

public class ProductDetailMapper {

    public static ProductDetail productDetailDTOToProductDetail(ProductDetailDTO productDetailDTO) {
        ProductDetail productDetail = new ProductDetail();
        productDetail.setIdDetProduct(productDetailDTO.getIdDetProduct());
        productDetail.setQuantity(productDetailDTO.getQuantity());
        productDetail.setInputUnitPrice(productDetailDTO.getInputUnitPrice());
        productDetail.setInputUnitPriceWithoutIVA(getPriceWithoutIva(productDetailDTO.getInputUnitPrice(), productDetailDTO.getIva()));
        productDetail.setOutputUnitPrice(productDetailDTO.getOutputUnitPrice());
        productDetail.setOutputUnitPriceWithoutIVA(getPriceWithoutIva(productDetailDTO.getOutputUnitPrice(), productDetailDTO.getIva()));
        productDetail.setBatch(productDetailDTO.getBatch());
        productDetail.setDueDate(productDetailDTO.getDueDate());
        return productDetail;
    }

    public static ProductDetailDTO productDetailToProductDetailDTO(ProductDetail productDetail) {
        ProductDetailDTO productDetailDTO = new ProductDetailDTO();
        productDetailDTO.setIdDetProduct(productDetail.getIdDetProduct());
        productDetailDTO.setQuantity(productDetail.getQuantity());
        productDetailDTO.setInputUnitPrice(productDetail.getInputUnitPrice());
        productDetailDTO.setInputUnitPriceWithoutIVA(productDetail.getInputUnitPriceWithoutIVA());
        productDetailDTO.setOutputUnitPrice(productDetail.getOutputUnitPrice());
        productDetailDTO.setOutputUnitPriceWithoutIVA(productDetail.getOutputUnitPriceWithoutIVA());
        productDetailDTO.setBatch(productDetail.getBatch());
        productDetailDTO.setDueDate(productDetail.getDueDate());
        return productDetailDTO;
    }

    private static Double getPriceWithoutIva(Double price, Integer iva) {
        return price / (1.0 + ((double)iva / 100));
    }
}
