package com.uptc.tc.eucaliptus.securityAPI.infraestructure.services;

import com.uptc.tc.eucaliptus.securityAPI.infraestructure.dtos.UpdateUserDTO;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.Message;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.User;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.repositories.UserRepository;
import com.uptc.tc.eucaliptus.securityAPI.utlities.ServicesUri;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Transactional
@Service
public class UserService {

    private UserRepository userRepository;
    private RestTemplate restTemplate;

    @Autowired
    public UserService(UserRepository userRepository, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    public Optional<User> getByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean existByUserName(String username) {
        return userRepository.existsByUsername(username);
    }

    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> updateUser(UpdateUserDTO userDetails) {
        return getByUserName(userDetails.getOldUsername()).map(user -> {
            user.setUsername(userDetails.getNewUsername());
            user.setEmail(userDetails.getEmail());
            return userRepository.save(user);
        });
    }

    public boolean updateSellerInfo(UpdateUserDTO userDetails, String token) {
        try{
            HttpEntity<UpdateUserDTO> entity = new HttpEntity<>(userDetails, getHeader(token));
            ResponseEntity<Message> response = restTemplate.exchange(
                    ServicesUri.PERSON_SERVICE + "/person/sellers/updateUserInfo",
                    HttpMethod.PUT,
                    entity,
                    Message.class
            );
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e){
            return false;
        }
    }

    public void delete(String username) {
        userRepository.delete(userRepository.findByUsername(username).get());
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
