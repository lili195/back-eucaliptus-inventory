package com.eucaliptus.springboot_app_reports.service;

import com.eucaliptus.springboot_app_reports.dto.DatesDTO;
import com.eucaliptus.springboot_app_reports.dto.ProductDTO;
import com.eucaliptus.springboot_app_reports.dto.ProductsSaleDTO;
import com.eucaliptus.springboot_app_reports.dto.ReportDTO;
import com.eucaliptus.springboot_app_reports.utlities.ServicesUri;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    public RestTemplate restTemplate;

    public List<ReportDTO> getReports(Date startDate, Date endDate, String token){
        List<ProductsSaleDTO> sales = this.getProductsSale(startDate, endDate, token);
        List<String> ids = sales.stream().map(ProductsSaleDTO::getProductId).toList();
        HttpEntity<List<String>> entity = new HttpEntity<>(ids, getHeader(token));
        ResponseEntity<List<ProductDTO>> response = restTemplate.exchange(
                ServicesUri.PRODUCT_SERVICE + "/products/getProductsById",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<ProductDTO>>() {}
        );
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null)
            return null;
        List<ProductDTO> products = response.getBody();
        return this.generateReportDTOs(sales, products);
    }

    private List<ProductsSaleDTO> getProductsSale(Date startDate, Date endDate, String token) {
        HttpEntity<DatesDTO> entity = new HttpEntity<>(new DatesDTO(startDate, endDate), getHeader(token));
        ResponseEntity<List<ProductsSaleDTO>> response = restTemplate.exchange(
                ServicesUri.BILLING_SERVICE + "/billing/sale/getProductsSale",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<ProductsSaleDTO>>() {}
        );
        if (response.getStatusCode().is2xxSuccessful())
            return response.getBody();
        return null;
    }

    private List<ReportDTO> generateReportDTOs(List<ProductsSaleDTO> productsSales, List<ProductDTO> products) {
        Map<String, ProductDTO> productMap = products.stream()
                .collect(Collectors.toMap(ProductDTO::getIdProduct, product -> product));
        List<ReportDTO> reports = new ArrayList<>();
        for (ProductsSaleDTO sale : productsSales) {
            ProductDTO product = productMap.get(sale.getProductId());
            if (product != null) {
                ReportDTO newReport = new ReportDTO();
                newReport.setProduct(product);
                newReport.setQuantity(sale.getQuantity());
                newReport.setTotalPrice(sale.getTotalPrice());
                reports.add(newReport);
            }
        }
        return reports;
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