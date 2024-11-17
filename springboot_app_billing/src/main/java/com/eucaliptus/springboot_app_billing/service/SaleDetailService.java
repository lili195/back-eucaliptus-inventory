package com.eucaliptus.springboot_app_billing.service;

import com.eucaliptus.springboot_app_billing.model.SaleDetail;
import com.eucaliptus.springboot_app_billing.repository.SaleDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SaleDetailService {

    @Autowired
    private SaleDetailRepository saleDetailRepository;

    public List<SaleDetail> getSalesBySale(Integer idSale) {
        return saleDetailRepository.findBySale_IdSale(idSale);
    }

    public List<SaleDetail> getSalesByIdProduct(String idProduct) {
        return saleDetailRepository.findByIdProduct(idProduct);
    }

    public Optional<SaleDetail> getSaleById(Integer id) {
        return saleDetailRepository.findById(id);
    }

    public SaleDetail saveSaleDetail(SaleDetail saleDetail) {
        return saleDetailRepository.save(saleDetail);
    }

}