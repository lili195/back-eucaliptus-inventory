package com.eucaliptus.springboot_app_billing.service;

import com.eucaliptus.springboot_app_billing.model.Sale;
import com.eucaliptus.springboot_app_billing.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    public List<Sale> getSalesByIdBill(Integer idBill) {
        return saleRepository.findByIdBill(idBill);
    }

    public List<Sale> getSalesByIdProduct(String idProduct) {
        return saleRepository.findByIdProduct(idProduct);
    }

    public Optional<Sale> getSaleById(Integer id) {
        return saleRepository.findById(id);
    }

    public Sale saveSale(Sale sale) {
        return saleRepository.save(sale);
    }

    public Optional<Sale> updateSale(Integer id, Sale saleDetails) {
        return saleRepository.findById(id).map(sale -> {
            sale.setQuantity(saleDetails.getQuantity());
            sale.setUnitPrice(saleDetails.getUnitPrice());
            sale.setIdProduct(saleDetails.getIdProduct());
            sale.setIdBill(saleDetails.getIdBill());
            return saleRepository.save(sale);
        });
    }

    public boolean deleteSale(Integer id) {
        if (saleRepository.existsById(id)) {
            saleRepository.deleteById(id);
            return true;
        }
        return false;
    }
}