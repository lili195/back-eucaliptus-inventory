package com.uptc.tc.eucaliptus.securityAPI.controller;

import com.uptc.tc.eucaliptus.securityAPI.controllers.AuthController;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.dtos.LoginUser;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.dtos.UserDTO;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.Role;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.User;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.enums.RoleList;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.services.EmailService;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.services.RecoveryCodeService;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.services.RoleService;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.services.UserService;
import com.uptc.tc.eucaliptus.securityAPI.security.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private RoleService roleService;

    @MockBean
    private EmailService emailService;

    @MockBean
    private RecoveryCodeService recoveryCodeService;

    @Test
    public void testLogin_Success() throws Exception {
        LoginUser loginUser = new LoginUser();
        loginUser.setUsername("user");
        loginUser.setPassword("password");

        when(userService.getByUserName("user")).thenReturn(Optional.of(new User("user", "user@example.com", "encodedPassword", new Role(RoleList.ROLE_ADMIN))));
        when(jwtProvider.generateToken(anyString(), any(), anyString())).thenReturn("fake-jwt-token");
        when(jwtProvider.getAllClaims(anyString())).thenReturn(mock(Claims.class));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.role").value("ROLE_ADMIN"));
    }

    @Test
    public void testLogin_Failure_InvalidCredentials() throws Exception {
        LoginUser loginUser = new LoginUser();
        loginUser.setUsername("user");
        loginUser.setPassword("wrongpassword");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user\",\"password\":\"wrongpassword\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Credenciales invalidas"));
    }

    @Test
    public void testAddSeller_Success() throws Exception {
        UserDTO userDTO = new UserDTO("newSeller", "newSeller@example.com", "password", "ROLE_SELLER");

        when(userService.getByEmail(anyString())).thenReturn(Optional.empty());
        when(userService.existByUserName(anyString())).thenReturn(false);
        when(emailService.sendEmailCredentials(anyString(), anyString(), anyString())).thenReturn(true);
        when(userService.save(any(User.class))).thenReturn(new User("newSeller", "newSeller@example.com", "encodedPassword", new Role(RoleList.ROLE_SELLER)));

        mockMvc.perform(post("/auth/addSeller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"newSeller\",\"email\":\"newSeller@example.com\",\"password\":\"password\",\"role\":\"ROLE_SELLER\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("newSeller"))
                .andExpect(jsonPath("$.email").value("newSeller@example.com"));
    }

    @Test
    public void testAddSeller_EmailAlreadyExists() throws Exception {
        UserDTO userDTO = new UserDTO("newSeller", "newSeller@example.com", "password", "ROLE_SELLER");

        when(userService.getByEmail(anyString())).thenReturn(Optional.of(new User("newSeller", "newSeller@example.com", "encodedPassword", new Role(RoleList.ROLE_ADMIN))));

        mockMvc.perform(post("/auth/addSeller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"newSeller\",\"email\":\"newSeller@example.com\",\"password\":\"password\",\"role\":\"ROLE_SELLER\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El emailClient ya existe"));
    }

    @Test
    public void testChangePassword_Success() throws Exception {
        LoginUser loginUser = new LoginUser();
        loginUser.setUsername("user");
        loginUser.setPassword("newpassword");
        String token = "valid-jwt-token";

        when(userService.existByUserName(anyString())).thenReturn(true);
        when(userService.getByUserName(anyString())).thenReturn(Optional.of(new User("user", "user@example.com", "encodedPassword", new Role(RoleList.ROLE_ADMIN))));
        when(jwtProvider.getUsernameFromToken(anyString())).thenReturn("user");

        mockMvc.perform(post("/auth/changePassword")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user\",\"password\":\"newpassword\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user"));
    }

    @Test
    public void testChangePassword_Failure_InvalidToken() throws Exception {
        String token = "invalid-jwt-token";

        mockMvc.perform(post("/auth/changePassword")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user\",\"password\":\"newpassword\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Token invalido"));
    }

    @Test
    public void testDeleteSeller_Success() throws Exception {
        String username = "sellerToDelete";

        when(userService.existByUserName(anyString())).thenReturn(true);

        mockMvc.perform(delete("/auth/deleteSeller")
                        .param("username", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Usuario eliminado existosamente"));
    }

    @Test
    public void testDeleteSeller_Failure_UserNotFound() throws Exception {
        String username = "nonExistingUser";

        when(userService.existByUserName(anyString())).thenReturn(false);

        mockMvc.perform(delete("/auth/deleteSeller")
                        .param("username", username))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El usuario no existe"));
    }



}

