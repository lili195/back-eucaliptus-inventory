package com.eucaliptus.springboot_app_person.services;


import com.eucaliptus.springboot_app_person.dtos.SellerDTO;
import com.eucaliptus.springboot_app_person.dtos.UserDTO;
import com.eucaliptus.springboot_app_person.model.Seller;
import com.eucaliptus.springboot_app_person.repository.SellerRepository;
import com.eucaliptus.springboot_app_person.utlities.ServicesUri;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.Optional;

@Service
public class SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private RestTemplate restTemplate;

    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    public Optional<Seller> getSellerById(Long id) {
        return sellerRepository.findById(id);
    }

    public Seller saveSeller(Seller seller) {
        return sellerRepository.save(seller);
    }

    public boolean createUser(SellerDTO seller, String token) {
        try{
            UserDTO userDTO = new UserDTO(
                    seller.getUsername(),
                    seller.getPersonDTO().getEmail(),
                    seller.getPassword(),
                    seller.getPersonDTO().getRole());
            HttpEntity<UserDTO> entity = new HttpEntity<>(userDTO, getHeader(token));
            ResponseEntity<UserDTO> response = restTemplate.exchange(
                    ServicesUri.AUTH_SERVICE + "/auth/addSeller",
                    HttpMethod.POST,
                    entity,
                    UserDTO.class
            );
            return response.getStatusCode() == HttpStatus.CREATED;
        } catch (Exception e){
            return false;
        }
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

    private HttpHeaders getHeader(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }

    public String getTokenByRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer "))
            token = authHeader.substring(7);
        return token;

    }
}
