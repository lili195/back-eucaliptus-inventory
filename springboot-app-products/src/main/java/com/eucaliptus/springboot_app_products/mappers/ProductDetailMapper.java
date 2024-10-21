package com.eucaliptus.springboot_app_products.mappers;

import com.eucaliptus.springboot_app_products.dto.ProductDetailDTO;
import com.eucaliptus.springboot_app_products.model.ProductDetail;

public class ProductDetailMapper {

    public static ProductDetail productDetailDTOToProductDetail(ProductDetailDTO productDetailDTO) {
        ProductDetail productDetail = new ProductDetail();
        productDetail.setIdDetProduct(productDetailDTO.getIdDetProduct());
        productDetail.setQuantity(productDetailDTO.getQuantity());
        productDetail.setUnitPrice(productDetailDTO.getUnitPrice());
        productDetail.setBatch(productDetailDTO.getBatch());
        productDetail.setDueDate(productDetailDTO.getDueDate());
        return productDetail;
    }

    public static ProductDetailDTO productDetailToProductDetailDTO(ProductDetail productDetail) {
        ProductDetailDTO productDetailDTO = new ProductDetailDTO();
        productDetailDTO.setIdDetProduct(productDetail.getIdDetProduct());
        productDetailDTO.setQuantity(productDetail.getQuantity());
        productDetailDTO.setUnitPrice(productDetail.getUnitPrice());
        productDetailDTO.setBatch(productDetail.getBatch());
        productDetailDTO.setDueDate(productDetail.getDueDate());
        return productDetailDTO;
    }
}
