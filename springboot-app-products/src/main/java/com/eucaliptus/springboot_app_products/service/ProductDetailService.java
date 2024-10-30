package com.eucaliptus.springboot_app_products.service;

import com.eucaliptus.springboot_app_products.model.ProductDetail;
import com.eucaliptus.springboot_app_products.repository.ProductDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductDetailService {

    @Autowired
    private ProductDetailRepository productDetailRepository;

    public List<ProductDetail> getAllProductDetails() {
        return productDetailRepository.findAll();
    }

    public Optional<ProductDetail> getProductDetailById(Long idDetProduct) {
        return productDetailRepository.findById(idDetProduct);
    }

    public ProductDetail saveProductDetail(ProductDetail productDetail) {
        return productDetailRepository.save(productDetail);
    }

    public Optional<ProductDetail> updateProductDetail(Long idDetProduct, ProductDetail productDetailDetails) {
        return productDetailRepository.findById(idDetProduct).map(productDetail -> {
            productDetail.setQuantity(productDetailDetails.getQuantity());
            productDetail.setInputUnitPrice(productDetailDetails.getInputUnitPrice());
            productDetail.setInputUnitPriceWithoutIVA(productDetailDetails.getInputUnitPriceWithoutIVA());
            productDetail.setOutputUnitPrice(productDetailDetails.getOutputUnitPrice());
            productDetail.setOutputUnitPriceWithoutIVA(productDetailDetails.getOutputUnitPriceWithoutIVA());
            productDetail.setBatch(productDetailDetails.getBatch());
            productDetail.setDueDate(productDetailDetails.getDueDate());
            return productDetailRepository.save(productDetail);
        });
    }

    public boolean existsByIdProductDetail(Long idDetProduct) {
        return productDetailRepository.existsById(idDetProduct);
    }

    public boolean deleteProductDetail(Long idDetProduct) {
        return productDetailRepository.findById(idDetProduct).map(productDetail -> {
            productDetailRepository.delete(productDetail);
            return true;
        }).orElse(false);
    }

    public boolean existsById(Long id) {
        return false;
    }
}
