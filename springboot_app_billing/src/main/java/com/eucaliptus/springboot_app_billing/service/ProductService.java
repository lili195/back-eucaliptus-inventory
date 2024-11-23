package com.eucaliptus.springboot_app_billing.service;

import com.eucaliptus.springboot_app_billing.dto.PurchaseDTO;
import com.eucaliptus.springboot_app_billing.dto.PurchaseDetailDTO;
import com.eucaliptus.springboot_app_billing.dto.SaleDTO;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private APIService apiService;
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

    public List<SaleDTO> getProductsFromSale(List<SaleDTO> sales, String token){
        Map<String, String> idsMap = new HashMap<>();
        for (SaleDTO sale : sales)
            for (SaleDetailDTO saleDetail : sale.getSaleDetails())
                idsMap.put(saleDetail.getIdProduct(), saleDetail.getIdProduct());
        List<ProductDTO> products = this.getProducts(idsMap.values().stream().toList(), token);
        Map<String, ProductDTO> productMap = products.stream()
                .collect(Collectors.toMap(ProductDTO::getIdProduct, product -> product));
        for (SaleDTO saleDetails : sales)
            for (SaleDetailDTO sale : saleDetails.getSaleDetails()) {
                ProductDTO product = productMap.get(sale.getIdProduct());
                if (product != null)
                    sale.setProductDTO(product);
            }
        return sales;
    }

    public List<PurchaseDTO> getProductsFromPurchase(List<PurchaseDTO> purchases, String token){
        Map<String, String> idsMap = new HashMap<>();
        for (PurchaseDTO purchase : purchases)
            for (PurchaseDetailDTO purchaseDetailDTO : purchase.getPurchaseDetails())
                idsMap.put(purchaseDetailDTO.getIdProduct(), purchaseDetailDTO.getIdProduct());
        List<ProductDTO> products = this.getProducts(idsMap.values().stream().toList(), token);
        Map<String, ProductDTO> productMap = products.stream()
                .collect(Collectors.toMap(ProductDTO::getIdProduct, product -> product));
        for (PurchaseDTO saleDetails : purchases)
            for (PurchaseDetailDTO purchase : saleDetails.getPurchaseDetails()) {
                ProductDTO product = productMap.get(purchase.getIdProduct());
                if (product != null)
                    purchase.setProductDTO(product);
            }
        return purchases;
    }

    private List<ProductDTO> getProducts(List<String> ids, String token){
        HttpEntity<List<String>> entity = new HttpEntity<>(ids, apiService.getHeader(token));
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

}
