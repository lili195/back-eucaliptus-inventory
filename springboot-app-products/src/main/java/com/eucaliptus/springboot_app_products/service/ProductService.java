package com.eucaliptus.springboot_app_products.service;

import com.eucaliptus.springboot_app_products.dto.Message;
import com.eucaliptus.springboot_app_products.dto.UpdateProductDTO;
import com.eucaliptus.springboot_app_products.dto.ProductDTO;
import com.eucaliptus.springboot_app_products.utlities.ServicesUri;
import com.eucaliptus.springboot_app_products.model.Product;
import com.eucaliptus.springboot_app_products.repository.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private RestTemplate restTemplate;
    private ProductRepository productRepository;

    public boolean createProduct(ProductDTO product, String token) {
        try{
            ProductDTO productDTO = new ProductDTO(
                    product.getProductName(),
                    product.getBrand(),
                    product.getCategory(),
                    product.getUse(),
                    product.getIdProvider(),
                    product.getDescription(),
                    product.getIdUnit(),
                    product.getMinimumProductAmount(),
                    product.getMaximumProductAmount());
            HttpEntity<ProductDTO> entity = new HttpEntity<>(productDTO, getHeader(token));
            ResponseEntity<ProductDTO> response = restTemplate.exchange(
                    ServicesUri.AUTH_SERVICE + "/auth/addProduct",
                    HttpMethod.POST,
                    entity,
                    ProductDTO.class
            );
            System.out.println(response.getStatusCode());
            System.out.println(HttpStatus.CREATED);
            return response.getStatusCode() == HttpStatus.CREATED;
        } catch (Exception e){
            return false;
        }
    }

    public boolean updateProduct(UpdateProductDTO productDetails, String token) {
        try{
            HttpEntity<UpdateProductDTO> entity = new HttpEntity<>(productDetails, getHeader(token));
            ResponseEntity<String> response = restTemplate.exchange(
                    ServicesUri.AUTH_SERVICE + "/auth/updateProduct",
                    HttpMethod.PUT,
                    entity,
                    String.class
            );
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProduct(String productName, String token) {
        try{
            String url = UriComponentsBuilder
                    .fromHttpUrl(ServicesUri.AUTH_SERVICE + "/auth/deleteProduct")
                    .queryParam("productName", productName)
                    .toUriString();
            HttpEntity<Message> entity = new HttpEntity<>(getHeader(token));
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    entity,
                    String.class
            );
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
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

    public List<Product> getAllActiveProducts() {
        return productRepository.findByActiveTrue();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findByIdProduct(id);
    }

    public Optional<Product> getProductByName(String name) {
        return productRepository.findByNameProduct(name);
    }

    public boolean existsByIdProduct(Long id) {
        return  productRepository.existsByIdProduct(id);
    }

    public boolean existsByNameProduct(String productName) {
        return productRepository.existsByName(productName);
    }
}
