package com.eucaliptus.springboot_app_products.service;

import com.eucaliptus.springboot_app_products.model.Product;
import com.eucaliptus.springboot_app_products.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long idProduct) {
        return productRepository.findById(idProduct);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> updateProduct(Long idProduct, Product productDetails) {
        return productRepository.findById(idProduct).map(product -> {
            product.setProductName(productDetails.getProductName());
            product.setBrand(productDetails.getBrand());
            product.setCategory(productDetails.getCategory());
            product.setUse(productDetails.getUse());
            product.setIdProvider(productDetails.getIdProvider());
            product.setDescription(productDetails.getDescription());
            product.setUnit(productDetails.getUnit());
            product.setMinimumProductAmount(productDetails.getMinimumProductAmount());
            product.setMaximumProductAmount(productDetails.getMaximumProductAmount());
            return productRepository.save(product);
        });
    }

    public boolean existsByIdProduct(Long idProduct) {
        return productRepository.existsById(idProduct);
    }

    public boolean deleteProduct(Long idProduct) {
        return productRepository.findById(idProduct).map(product -> {
            productRepository.delete(product);
            return true;
        }).orElse(false);
    }

    public boolean existsById(Long id) {
        return false;
    }
}
