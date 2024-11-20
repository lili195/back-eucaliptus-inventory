package com.eucaliptus.springboot_app_billing.service;

import com.eucaliptus.springboot_app_billing.dto.PurchaseDetailDTO;
import com.eucaliptus.springboot_app_billing.dto.SaleDetailDTO;
import com.eucaliptus.springboot_app_billing.utlities.ServicesUri;
import com.eucaliptus.springboot_app_products.dto.ProductDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private RestTemplate restTemplate;

    public List<SaleDetailDTO> getSaleDetails(List<SaleDetailDTO> sales, String token){
        List<ProductDTO> products = this.getProducts(sales.stream().
                map(SaleDetailDTO::getIdProduct).toList(), token);
        Map<String, ProductDTO> productMap = products.stream()
                .collect(Collectors.toMap(ProductDTO::getIdProduct, product -> product));
        for (SaleDetailDTO sale : sales) {
            ProductDTO product = productMap.get(sale.getIdProduct());
            if (product != null)
                sale.setProductDTO(product);
        }
        return sales;
    }

    public List<PurchaseDetailDTO> getPurchaseDetails(List<PurchaseDetailDTO> purchases, String token){
        List<ProductDTO> products = this.getProducts(purchases.stream().
                map(PurchaseDetailDTO::getIdProduct).toList(), token);
        Map<String, ProductDTO> productMap = products.stream()
                .collect(Collectors.toMap(ProductDTO::getIdProduct, product -> product));
        for (PurchaseDetailDTO purchase : purchases) {
            ProductDTO product = productMap.get(purchase.getIdProduct());
            if (product != null)
                purchase.setProductDTO(product);
        }
        return purchases;
    }

    private List<ProductDTO> getProducts(List<String> ids, String token){
        HttpEntity<List<String>> entity = new HttpEntity<>(ids, getHeader(token));
        ResponseEntity<List<ProductDTO>> response = restTemplate.exchange(
                ServicesUri.PRODUCT_SERVICE + "/products/getProductsById",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<List<ProductDTO>>() {}
        );
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null)
            return null;
        return response.getBody();
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
}
