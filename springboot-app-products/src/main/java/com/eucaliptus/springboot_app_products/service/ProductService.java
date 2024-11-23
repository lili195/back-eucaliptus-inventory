package com.eucaliptus.springboot_app_products.service;

import com.eucaliptus.springboot_app_person.dtos.ProviderDTO;
import com.eucaliptus.springboot_app_products.dto.ProductExpiringSoonDTO;
import com.eucaliptus.springboot_app_products.utlities.ServicesUri;
import com.eucaliptus.springboot_app_products.model.Product;
import com.eucaliptus.springboot_app_products.repository.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private APIService apiService;

    public List<Product> getAllActiveProducts() {
        return productRepository.findByActiveTrue();
    }

    public List<Product> getProductsByIdProvider(String idProvider) {
        return productRepository.findByIdProviderAndActiveTrue(idProvider);
    }

    public Optional<Product> getProductById(String id) {
        return productRepository.findByIdProduct(id);
    }

    public Optional<Product> getProductByName(String name) {
        return productRepository.findByProductName(name);
    }

    public boolean existsByIdProduct(String id) {
        return  productRepository.existsByIdProduct(id);
    }

    public boolean existsByNameProduct(String productName) {
        return productRepository.existsByProductName(productName);
    }

    public boolean existsProviderId(String  providerId, String token) {
        try{
            HttpEntity<String> entity = new HttpEntity<>(apiService.getHeader(token));
            ResponseEntity<ProviderDTO> response = restTemplate.exchange(
                ServicesUri.PERSON_SERVICE + "/person/providers/getProviderById/" + providerId,
                    HttpMethod.GET,
                    entity,
                    ProviderDTO.class
            );
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> updateProduct(String id, Product productDetails) {
        return productRepository.findByIdProduct(id).map(product -> {
            product.setProductName(productDetails.getProductName());
            product.setBrand(productDetails.getBrand());
            product.setCategory(productDetails.getCategory());
            product.setUse(productDetails.getUse());
            product.setDescription(productDetails.getDescription());
            product.setUnit(productDetails.getUnit());
            product.setMinimumProductAmount(productDetails.getMinimumProductAmount());
            product.setMaximumProductAmount(productDetails.getMaximumProductAmount());
            return productRepository.save(product);
        });
    }

    public boolean deleteProduct(String id) {
        return productRepository.findByIdProduct(id).map(product -> {
           product.setActive(false);
           productRepository.save(product);
           return true;
        }).orElse(false);
    }

    @Transactional
    public boolean deleteProductsByIdProvider(String idProvider) {
        List<Product> products = getProductsByIdProvider(idProvider);
        for (Product product : products)
            if (!deleteProduct(product.getIdProduct()))
                throw new IllegalArgumentException("Producto no encontrado");
        return true;
    }

    public List<ProductExpiringSoonDTO> getProductsExpiringSoon() {
        return productRepository.findProductsExpiringSoon();
    }
}
