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

/**
 * Servicio para la gestión de operaciones relacionadas con los usuarios.
 * <p>
 * Proporciona métodos para realizar CRUD de usuarios, actualizaciones de información,
 * y comunicación con otros servicios utilizando {@link RestTemplate}.
 * </p>
 */

@Transactional
@Service
public class UserService {

    private UserRepository userRepository;
    private RestTemplate restTemplate;

    /**
     * Constructor que inyecta las dependencias requeridas.
     *
     * @param userRepository Repositorio de usuarios para interactuar con la base de datos.
     * @param restTemplate   Cliente REST para comunicación con otros servicios.
     */

    @Autowired
    public UserService(UserRepository userRepository, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    /**
     * Obtiene un usuario por su nombre de usuario.
     *
     * @param username Nombre de usuario a buscar.
     * @return Un {@link Optional} que contiene el usuario si existe.
     */

    public Optional<User> getByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Verifica si existe un usuario con el nombre de usuario proporcionado.
     *
     * @param username Nombre de usuario a verificar.
     * @return {@code true} si el usuario existe, {@code false} en caso contrario.
     */

    public boolean existByUserName(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Obtiene un usuario por su correo electrónico.
     *
     * @param email Correo electrónico a buscar.
     * @return Un {@link Optional} que contiene el usuario si existe.
     */

    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Guarda un usuario en la base de datos.
     *
     * @param user El usuario a guardar.
     * @return El usuario guardado.
     */

    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Actualiza la información de un usuario existente.
     *
     * @param userDetails DTO con los datos del usuario a actualizar.
     * @return Un {@link Optional} que contiene el usuario actualizado si fue encontrado.
     */

    public Optional<User> updateUser(UpdateUserDTO userDetails) {
        return getByUserName(userDetails.getOldUsername()).map(user -> {
            user.setUsername(userDetails.getNewUsername());
            user.setEmail(userDetails.getEmail());
            return userRepository.save(user);
        });
    }

    /**
     * Actualiza la información de un vendedor mediante un servicio externo.
     *
     * @param userDetails DTO con los datos del vendedor a actualizar.
     * @param token       Token de autenticación para la solicitud.
     * @return {@code true} si la actualización fue exitosa, {@code false} en caso contrario.
     */

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

    /**
     * Elimina un usuario por su nombre de usuario.
     *
     * @param username Nombre de usuario del usuario a eliminar.
     */

    public void delete(String username) {
        userRepository.delete(userRepository.findByUsername(username).get());
    }

    /**
     * Construye los encabezados de una solicitud HTTP con un token de autenticación.
     *
     * @param token Token de autenticación.
     * @return Encabezados HTTP configurados.
     */

    private HttpHeaders getHeader(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }

    /**
     * Extrae el token de autenticación del encabezado de una solicitud HTTP.
     *
     * @param request La solicitud HTTP.
     * @return El token de autenticación, o {@code null} si no está presente.
     */

    public String getTokenByRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer "))
            token = authHeader.substring(7);
        return token;
    }

}
