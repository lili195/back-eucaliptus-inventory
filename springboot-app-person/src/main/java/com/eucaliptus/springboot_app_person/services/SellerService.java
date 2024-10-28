package com.eucaliptus.springboot_app_person.services;

import com.eucaliptus.springboot_app_person.dtos.Message;
import com.eucaliptus.springboot_app_person.dtos.SellerDTO;
import com.eucaliptus.springboot_app_person.dtos.UserDTO;
import com.eucaliptus.springboot_app_person.model.Seller;
import com.eucaliptus.springboot_app_person.repository.SellerRepository;
import com.eucaliptus.springboot_app_person.utlities.ServicesUri;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.swing.text.html.parser.Entity;
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
        return sellerRepository.findByActiveTrue();
    }

    public Optional<Seller> getSellerById(Long id) {
        return sellerRepository.findById(id);
    }

    public Optional<Seller> getSellerByPersonId(String personId) {
        return sellerRepository.findByPerson_IdNumber(personId);
    }

    public Optional<Seller> getSellerByUsername(String username) {
        return sellerRepository.getByUsername(username);
    }

    public Seller saveSeller(Seller seller) {
        return sellerRepository.save(seller);
    }

    public Optional<Seller> updateSeller(Long id, Seller sellerDetails) {
        return sellerRepository.findById(id).map(seller -> {
            seller.setUsername(sellerDetails.getUsername());
            seller.setHomeAddress(sellerDetails.getHomeAddress());
            return sellerRepository.save(seller);
        });
    }

    public boolean existsById(Long id) {
        return sellerRepository.existsById(id);
    }

    public boolean existsByUsername(String username){
        return sellerRepository.existsByUsername(username);
    }

    public boolean deleteSeller(Long id, String token) {
        return sellerRepository.findById(id).map(seller -> {
            seller.setActive(false);
            seller.getPerson().setActive(false);
            sellerRepository.save(seller);
            return true;
        }).orElse(false);
    }


}
