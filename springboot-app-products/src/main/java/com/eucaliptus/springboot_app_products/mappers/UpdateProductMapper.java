package com.eucaliptus.springboot_app_products.mappers;

import com.eucaliptus.springboot_app_products.dto.UpdateProductDTO;
import com.eucaliptus.springboot_app_products.model.Product;

public class UpdateProductMapper {

    public static Product updateProductDTOToProduct(UpdateProductDTO updateProductDTO, Product existingProduct) {
        existingProduct.setName(updateProductDTO.getProductName());
        existingProduct.setDescription(updateProductDTO.getDescription());
        existingProduct.setIdUnit(updateProductDTO.getIdUnit());
        existingProduct.setMinimumProductAmount(updateProductDTO.getMinimumProductAmount());
        existingProduct.setMaximumProductAmount(updateProductDTO.getMaximumProductAmount());
        return existingProduct;
    }

    public static UpdateProductDTO productToUpdateProductDTO(Product product) {
        UpdateProductDTO updateProductDTO = new UpdateProductDTO();
        updateProductDTO.setProductName(product.getName());
        updateProductDTO.setDescription(product.getDescription());
        updateProductDTO.setIdUnit(product.getIdUnit());
        updateProductDTO.setMinimumProductAmount(product.getMinimumProductAmount());
        updateProductDTO.setMaximumProductAmount(product.getMaximumProductAmount());
        return updateProductDTO;
    }
}
