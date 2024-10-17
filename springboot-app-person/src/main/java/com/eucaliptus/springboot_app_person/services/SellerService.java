package com.eucaliptus.springboot_app_person.services;


import com.eucaliptus.springboot_app_person.model.Seller;
import com.eucaliptus.springboot_app_person.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    public Optional<Seller> getSellerById(Long id) {
        return sellerRepository.findById(id);
    }

    public Seller saveSeller(Seller seller) {
        return sellerRepository.save(seller);
    }

    public Optional<Seller> updateSeller(Long id, Seller sellerDetails) {
        return sellerRepository.findById(id).map(seller -> {
            seller.setUsername(sellerDetails.getUsername());
            seller.setHomeAddress(sellerDetails.getHomeAddress());
            seller.setDocumentType(sellerDetails.getDocumentType());
            return sellerRepository.save(seller);
        });
    }

    public boolean existsById(Long id) {
        return sellerRepository.existsById(id);
    }

    public boolean deleteSeller(Long id) {
        return sellerRepository.findById(id).map(seller -> {
            sellerRepository.delete(seller);
            return true;
        }).orElse(false);
    }
}
