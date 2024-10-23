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

    @Autowired
    private RestTemplate restTemplate;

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
            System.out.println(response.getStatusCode());
            System.out.println(HttpStatus.CREATED);
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

    public boolean existsByUsername(String username){
        return sellerRepository.existsByUsername(username);
    }

    public boolean deleteSeller(Long id, String token) {
        return sellerRepository.findById(id).map(seller -> {
            seller.setActive(false);
            seller.getPerson().setActive(false);
            sellerRepository.save(seller);
            return deleteUserAccount(seller.getUsername(), token);
        }).orElse(false);
    }

    public boolean deleteUserAccount(String username, String token) {
        try{
            String url = UriComponentsBuilder
                    .fromHttpUrl(ServicesUri.AUTH_SERVICE + "/auth/deleteSeller")
                    .queryParam("username", username)
                    .toUriString();
            HttpEntity<Message> entity = new HttpEntity<>(getHeader(token));
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    entity,
                    String.class
            );
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
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
