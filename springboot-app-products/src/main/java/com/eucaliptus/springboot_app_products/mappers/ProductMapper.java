package com.eucaliptus.springboot_app_products.mappers;

import com.eucaliptus.springboot_app_products.dto.ProductDTO;
import com.eucaliptus.springboot_app_products.model.Product;
import com.eucaliptus.springboot_app_products.model.Unit;

public class ProductMapper {

    public static Product productDTOToProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setIdProduct(productDTO.getIdProduct());
        product.setProductName(productDTO.getProductName());
        product.setBrand(productDTO.getBrand());
        product.setCategory(productDTO.getCategory());
        product.setUse(productDTO.getUse());
        product.setIdProvider(productDTO.getIdProvider());
        product.setDescription(productDTO.getDescription());
        product.setUnit(new Unit());
        product.setMinimumProductAmount(productDTO.getMinimumProductAmount());
        product.setMaximumProductAmount(productDTO.getMaximumProductAmount());
        return product;
    }

    public static ProductDTO productToProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setIdProduct(product.getIdProduct());
        productDTO.setProductName(product.getProductName());
        productDTO.setBrand(product.getBrand());
        productDTO.setCategory(product.getCategory());
        productDTO.setUse(product.getUse());
        productDTO.setIdProvider(product.getIdProvider());
        productDTO.setDescription(product.getDescription());
        productDTO.setIdUnit(product.getUnit().getIdUnit());
        productDTO.setMinimumProductAmount(product.getMinimumProductAmount());
        productDTO.setMaximumProductAmount(product.getMaximumProductAmount());
        return productDTO;
    }
}
