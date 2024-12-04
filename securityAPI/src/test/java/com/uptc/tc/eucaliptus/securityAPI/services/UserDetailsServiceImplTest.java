package com.uptc.tc.eucaliptus.securityAPI.services;

import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.Role;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.User;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.enums.RoleList;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.services.UserDetailsServiceImpl;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {

    @Mock
    private UserService userService;

    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDetailsService = new UserDetailsServiceImpl(userService);
    }

    @Test
    void testLoadUserByUsername_UserExists() {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setRole(new Role(RoleList.ROLE_SELLER));

        when(userService.getByUserName(username)).thenReturn(java.util.Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_SELLER")));
        verify(userService, times(1)).getByUserName(username);
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        String username = "nonexistentuser";

        when(userService.getByUserName(username)).thenReturn(java.util.Optional.empty());

        UsernameNotFoundException thrown = assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername(username)
        );

        assertEquals("El usuario nonexistentuser no existe", thrown.getMessage());
        verify(userService, times(1)).getByUserName(username);
    }
}
