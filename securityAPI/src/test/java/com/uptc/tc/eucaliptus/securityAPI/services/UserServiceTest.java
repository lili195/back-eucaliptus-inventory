package com.uptc.tc.eucaliptus.securityAPI.services;

import com.uptc.tc.eucaliptus.securityAPI.infraestructure.dtos.UpdateUserDTO;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.Message;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.User;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.repositories.UserRepository;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestTemplate restTemplate;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, restTemplate);
    }

    @Test
    void testGetByUserName() {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getByUserName(username);

        assertTrue(result.isPresent());
        assertEquals(username, result.get().getUsername());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testExistByUserName() {
        String username = "testuser";

        when(userRepository.existsByUsername(username)).thenReturn(true);

        boolean exists = userService.existByUserName(username);

        assertTrue(exists);
        verify(userRepository, times(1)).existsByUsername(username);
    }

    @Test
    void testSaveUser() {
        User user = new User();
        user.setUsername("testuser");

        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.save(user);

        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUser() {
        String oldUsername = "oldUsername";
        UpdateUserDTO userDetails = new UpdateUserDTO();
        userDetails.setOldUsername(oldUsername);
        userDetails.setNewUsername("newUsername");
        userDetails.setEmail("newemail@example.com");

        User existingUser = new User();
        existingUser.setUsername(oldUsername);

        when(userRepository.findByUsername(oldUsername)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        Optional<User> updatedUser = userService.updateUser(userDetails);

        assertTrue(updatedUser.isPresent());
        assertEquals("newUsername", updatedUser.get().getUsername());
        assertEquals("newemail@example.com", updatedUser.get().getEmail());
        verify(userRepository, times(1)).findByUsername(oldUsername);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testUpdateSellerInfo() {
        String token = "validToken";
        UpdateUserDTO userDetails = new UpdateUserDTO();
        userDetails.setOldUsername("oldUsername");

        ResponseEntity<Message> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(
                anyString(), eq(HttpMethod.PUT), any(), eq(Message.class)))
                .thenReturn(responseEntity);

        boolean result = userService.updateSellerInfo(userDetails, token);

        assertTrue(result);
        verify(restTemplate, times(1)).exchange(
                anyString(), eq(HttpMethod.PUT), any(), eq(Message.class));
    }

    @Test
    void testDeleteUser() {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        userService.delete(username);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testGetTokenByRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String expectedToken = "Bearer token123";
        when(request.getHeader("Authorization")).thenReturn(expectedToken);

        String token = userService.getTokenByRequest(request);

        assertEquals("token123", token);
        verify(request, times(1)).getHeader("Authorization");
    }
}
