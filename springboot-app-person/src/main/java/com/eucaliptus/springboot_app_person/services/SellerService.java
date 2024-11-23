package com.eucaliptus.springboot_app_person.services;

import com.eucaliptus.springboot_app_person.enums.EnumRole;
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

    public List<Seller> getAllActiveSellers() {
        return sellerRepository.findByActiveTrueAndRole(EnumRole.ROLE_SELLER);
    }

    public Optional<Seller> getSellerById(String id) {
        return sellerRepository.findById(id);
    }

    public Optional<Seller> getSellerByPersonId(String personId) {
        return sellerRepository.findByIdNumber(personId);
    }

    public Optional<Seller> getSellerByUsername(String username) {
        return sellerRepository.getByUsername(username);
    }

    public Seller saveSeller(Seller seller) {
        return sellerRepository.save(seller);
    }

    public Optional<Seller> updateSeller(String id, Seller sellerDetails) {
        return sellerRepository.findByIdNumber(id).map(seller -> {
            seller.setFirstName(sellerDetails.getFirstName());
            seller.setLastName(sellerDetails.getLastName());
            seller.setEmail(sellerDetails.getEmail());
            seller.setAddress(sellerDetails.getAddress());
            seller.setPhoneNumber(sellerDetails.getPhoneNumber());
            seller.setDocumentType(sellerDetails.getDocumentType());
            seller.setUsername(sellerDetails.getUsername());
            return sellerRepository.save(seller);
        });
    }

    public boolean existsById(String id) {
        return sellerRepository.existsByIdNumber(id);
    }

    public boolean existsByUsername(String username){
        return sellerRepository.existsByUsername(username);
    }

        public boolean deleteSeller(String id, String token) {
        return sellerRepository.findByIdNumber(id).map(seller -> {
            seller.setActive(false);
            sellerRepository.save(seller);
            return true;
        }).orElse(false);
    }


}
