package com.uptc.tc.eucaliptus.securityAPI.services;

import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.Role;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.enums.RoleList;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.repositories.RoleRepository;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.services.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    private RoleService roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        roleService = new RoleService(roleRepository);
    }

    @Test
    void testGetByRoleName_RoleExists() {
        RoleList roleName = RoleList.ROLE_ADMIN;
        Role role = new Role();
        role.setRoleName(roleName);

        when(roleRepository.findByRoleName(roleName)).thenReturn(java.util.Optional.of(role));

        Role result = roleService.getByRoleName(roleName);

        assertNotNull(result);
        assertEquals(roleName, result.getRoleName());
        verify(roleRepository, times(1)).findByRoleName(roleName);
    }

    @Test
    void testGetByRoleName_RoleNotFound() {
        RoleList roleName = RoleList.ROLE_ADMIN;

        when(roleRepository.findByRoleName(roleName)).thenReturn(java.util.Optional.empty());

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () ->
                roleService.getByRoleName(roleName)
        );

        assertEquals("No value present", thrown.getMessage());
        verify(roleRepository, times(1)).findByRoleName(roleName);
    }

    @Test
    void testSave_NewRole() {
        Role role = new Role();
        role.setRoleName(RoleList.ROLE_SELLER);

        when(roleRepository.existsByRoleName(role.getRoleName())).thenReturn(false);
        when(roleRepository.save(role)).thenReturn(role);

        Role savedRole = roleService.save(role);

        assertNotNull(savedRole);
        assertEquals(role.getRoleName(), savedRole.getRoleName());
        verify(roleRepository, times(1)).existsByRoleName(role.getRoleName());
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void testSave_ExistingRole() {
        Role role = new Role();
        role.setRoleName(RoleList.ROLE_SELLER);

        when(roleRepository.existsByRoleName(role.getRoleName())).thenReturn(true);
        when(roleRepository.findByRoleName(role.getRoleName())).thenReturn(java.util.Optional.of(role));

        Role savedRole = roleService.save(role);

        assertNotNull(savedRole);
        assertEquals(role.getRoleName(), savedRole.getRoleName());
        verify(roleRepository, times(1)).existsByRoleName(role.getRoleName());
        verify(roleRepository, times(0)).save(role);
        verify(roleRepository, times(1)).findByRoleName(role.getRoleName());
    }
}
