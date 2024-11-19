package com.eucaliptus.springboot_app_products.service;

import com.eucaliptus.springboot_app_products.model.ExpiringProduct;
import com.eucaliptus.springboot_app_products.repository.ExpiringProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpiringProductService {

    @Autowired
    private ExpiringProductRepository repository;

    public List<ExpiringProduct> getExpiringProducts() {
        return repository.findProductsExpiringInSevenDays();
    }
}
