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

/**
 * Servicio encargado de gestionar las operaciones relacionadas con los usuarios.
 * Esta clase proporciona métodos para:
 * - Crear un nuevo usuario.
 * - Actualizar la información de un usuario existente.
 * - Eliminar una cuenta de usuario.
 */
@Service
public class UserService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private APIService apiService;

    /**
     * Crea un nuevo usuario basado en los datos de un vendedor.
     *
     * Este método envía una solicitud POST al servicio de autenticación para crear un nuevo usuario con los detalles proporcionados en el {@code SellerDTO}.
     * El rol del nuevo usuario se asigna como {@code ROLE_SELLER}.
     *
     * @param seller El DTO del vendedor que contiene los datos necesarios para crear el usuario.
     * @param token El token de autenticación que se usará para autorizar la solicitud.
     * @return {@code true} si la creación del usuario fue exitosa (código de estado HTTP 201), {@code false} si ocurrió un error.
     */
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

    /**
     * Actualiza la información de un usuario existente.
     *
     * Este método envía una solicitud PUT al servicio de autenticación para actualizar la información del usuario basado en el DTO {@code UpdateUserDTO}.
     *
     * @param userDetails El DTO que contiene los nuevos detalles del usuario a actualizar.
     * @param token El token de autenticación que se usará para autorizar la solicitud.
     * @return {@code true} si la actualización del usuario fue exitosa (código de estado HTTP 200), {@code false} si ocurrió un error.
     */
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

    /**
     * Elimina la cuenta de un usuario.
     *
     * Este método envía una solicitud DELETE al servicio de autenticación para eliminar la cuenta de un vendedor usando su nombre de usuario.
     *
     * @param username El nombre de usuario del vendedor cuya cuenta se desea eliminar.
     * @param token El token de autenticación que se usará para autorizar la solicitud.
     * @return {@code true} si la eliminación de la cuenta fue exitosa (código de estado HTTP 200), {@code false} si ocurrió un error.
     */
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
