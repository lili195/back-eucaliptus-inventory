package com.eucaliptus.springboot_app_billing.service;

import com.eucaliptus.springboot_app_billing.model.PurchaseDetail;
import com.eucaliptus.springboot_app_billing.repository.PurchaseDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseDetailService {

    @Autowired
    private PurchaseDetailRepository purchaseDetailRepository;

    public List<PurchaseDetail> findAll() {
        return purchaseDetailRepository.findAll();
    }

    public List<PurchaseDetail> findByPurchaseId(Integer purchaseId) {
        return purchaseDetailRepository.findByPurchase_IdPurchase(purchaseId);
    }

    public PurchaseDetail savePurchaseDetail(PurchaseDetail purchaseDetail) {
        return purchaseDetailRepository.save(purchaseDetail);
    }
}
