package com.eucaliptus.springboot_app_person.services;

import com.eucaliptus.springboot_app_person.dtos.Message;
import com.eucaliptus.springboot_app_person.dtos.SellerDTO;
import com.eucaliptus.springboot_app_person.dtos.UpdateUserDTO;
import com.eucaliptus.springboot_app_person.dtos.UserDTO;
import com.eucaliptus.springboot_app_person.enums.EnumRole;
import com.eucaliptus.springboot_app_person.utlities.ServicesUri;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class UserService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private APIService apiService;

    public boolean createUser(SellerDTO seller, String token) {
        try{
            UserDTO userDTO = new UserDTO(
                    seller.getUsername(),
                    seller.getPersonDTO().getEmail(),
                    seller.getPassword(),
                    EnumRole.ROLE_SELLER.name());
            HttpEntity<UserDTO> entity = new HttpEntity<>(userDTO, apiService.getHeader(token));
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
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUserInfo(UpdateUserDTO userDetails, String token) {
        try{
            HttpEntity<UpdateUserDTO> entity = new HttpEntity<>(userDetails, apiService.getHeader(token));
            ResponseEntity<String> response = restTemplate.exchange(
                    ServicesUri.AUTH_SERVICE + "/auth/updateUserInfo",
                    HttpMethod.PUT,
                    entity,
                    String.class
            );
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUserAccount(String username, String token) {
        try{
            String url = UriComponentsBuilder
                    .fromHttpUrl(ServicesUri.AUTH_SERVICE + "/auth/deleteSeller")
                    .queryParam("username", username)
                    .toUriString();
            HttpEntity<Message> entity = new HttpEntity<>(apiService.getHeader(token));
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

}
