package com.uptc.tc.eucaliptus.securityAPI.repository;

import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.Role;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.enums.RoleList;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.repositories.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    private Role testRole;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testRole = new Role(RoleList.ROLE_ADMIN);
        testRole.setId(1);
    }

    @Test
    void testFindByRoleName() {
        when(roleRepository.findByRoleName(RoleList.ROLE_ADMIN)).thenReturn(Optional.of(testRole));

        Optional<Role> result = roleRepository.findByRoleName(RoleList.ROLE_ADMIN);

        assertTrue(result.isPresent());
        assertEquals(RoleList.ROLE_ADMIN, result.get().getRoleName());
        assertEquals(1, result.get().getId());

        verify(roleRepository, times(1)).findByRoleName(RoleList.ROLE_ADMIN);
    }

    @Test
    void testExistsByRoleName() {
        when(roleRepository.existsByRoleName(RoleList.ROLE_ADMIN)).thenReturn(true);

        boolean exists = roleRepository.existsByRoleName(RoleList.ROLE_ADMIN);

        assertTrue(exists);

        verify(roleRepository, times(1)).existsByRoleName(RoleList.ROLE_ADMIN);
    }
}
