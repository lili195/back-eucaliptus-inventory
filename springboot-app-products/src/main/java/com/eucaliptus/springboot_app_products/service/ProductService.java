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
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ProductRepository productRepository;

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
        return productRepository.findByName(name);
    }

    public boolean existsByIdProduct(String id) {
        return  productRepository.existsByIdProduct(id);
    }

    public boolean existsByNameProduct(String productName) {
        return productRepository.existsByName(productName);
    }

    public boolean existsProviderId(String  providerId, String token) {
        try{
            HttpEntity<String> entity = new HttpEntity<>(getHeader(token));
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
            product.setName(productDetails.getName());
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

    public String getTokenByRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer "))
            token = authHeader.substring(7);
        return token;
    }

    private HttpHeaders getHeader(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }

    public List<ProductExpiringSoonDTO> getProductsExpiringSoon() {
        return productRepository.findProductsExpiringSoon();
    }
}
