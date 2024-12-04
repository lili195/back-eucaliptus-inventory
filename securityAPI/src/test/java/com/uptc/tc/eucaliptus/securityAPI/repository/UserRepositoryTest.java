package com.uptc.tc.eucaliptus.securityAPI.repository;

import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.Role;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.User;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.enums.RoleList;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Role role = new Role(RoleList.ROLE_SELLER);
        role.setId(1);

        testUser = new User("testSeller", "seller@example.com", "securePassword", role);
        testUser.setId("123e4567-e89b-12d3-a456-426614174001");
    }

    @Test
    void testFindByUsername() {
        String username = "testSeller";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(testUser));

        Optional<User> result = userRepository.findByUsername(username);
        assertTrue(result.isPresent());
        assertEquals(username, result.get().getUsername());
        assertEquals(RoleList.ROLE_SELLER, result.get().getRole().getRoleName());

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testFindByEmail() {
        String email = "seller@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));

        Optional<User> result = userRepository.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
        assertEquals(RoleList.ROLE_SELLER, result.get().getRole().getRoleName());

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testExistsByUsername() {
        String username = "testSeller";

        when(userRepository.existsByUsername(username)).thenReturn(true);

        boolean exists = userRepository.existsByUsername(username);
        assertTrue(exists);

        verify(userRepository, times(1)).existsByUsername(username);
    }
}
