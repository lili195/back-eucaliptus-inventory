package com.eucaliptus.springboot_app_billing.service;

import com.eucaliptus.springboot_app_billing.model.Bill;
import com.eucaliptus.springboot_app_billing.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    public List<Bill> getBillsByClientId(Integer idClient) {
        return billRepository.findByIdClient(idClient);
    }

    public List<Bill> getBillsByDate(Date date) {
        return billRepository.findByBillDate(date);
    }

    public Optional<Bill> getBillById(Integer id) {
        return billRepository.findById(id);
    }

    public Bill saveBill(Bill bill) {
        return billRepository.save(bill);
    }

    public Optional<Bill> updateBill(Integer id, Bill billDetails) {
        return billRepository.findById(id).map(bill -> {
            bill.setBillDate(billDetails.getBillDate());
            bill.setTotal(billDetails.getTotal());
            bill.setIdPerson(billDetails.getIdPerson());
            bill.setIdClient(billDetails.getIdClient());
            return billRepository.save(bill);
        });
    }

    public boolean deleteBill(Integer id) {
        if (billRepository.existsById(id)) {
            billRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
