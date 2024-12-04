package com.uptc.tc.eucaliptus.securityAPI.repository;

import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.Role;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.enums.RoleList;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.repositories.RecoveryCodeRepository;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.RecoveryCode;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecoveryCodeRepositoryTest {

    @Mock
    private RecoveryCodeRepository recoveryCodeRepository;

    private RecoveryCode testRecoveryCode;
    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Role role = new Role(RoleList.ROLE_SELLER); // Cambiado para reflejar el nuevo rol
        role.setId(1); // Configurar el ID manualmente para las pruebas

        // Crear un usuario de prueba
        testUser = new User("testSeller", "seller@example.com", "securePassword", role);
        testUser.setId("123e4567-e89b-12d3-a456-426614174001");

        // Crear un código de recuperación de prueba
        testRecoveryCode = new RecoveryCode(456789, testUser, LocalDateTime.now().plusMinutes(30));
        testRecoveryCode.setId("test-id");
    }

    @Test
    void testFindByCodeAndExpiryDateAfter() {
        LocalDateTime now = LocalDateTime.now();

        // Configurar el mock
        when(recoveryCodeRepository.findByCodeAndExpiryDateAfter(456789, now))
                .thenReturn(Optional.of(testRecoveryCode));

        // Llamar al método
        Optional<RecoveryCode> result = recoveryCodeRepository.findByCodeAndExpiryDateAfter(456789, now);

        // Validar resultados
        assertTrue(result.isPresent());
        assertEquals(456789, result.get().getCode());
        assertEquals("test-id", result.get().getId());

        // Verificar interacción
        verify(recoveryCodeRepository, times(1))
                .findByCodeAndExpiryDateAfter(456789, now);
    }

    @Test
    void testFindByUserId() {
        // Configurar el mock
        when(recoveryCodeRepository.findByUserId("123"))
                .thenReturn(Optional.of(testRecoveryCode));

        // Llamar al método
        Optional<RecoveryCode> result = recoveryCodeRepository.findByUserId("123");

        // Validar resultados
        assertTrue(result.isPresent());
        assertEquals("test-id", result.get().getId());
        assertEquals(456789, result.get().getCode());

        // Verificar interacción
        verify(recoveryCodeRepository, times(1)).findByUserId("123");
    }

    @Test
    void testExistsByCode() {
        // Configurar el mock
        when(recoveryCodeRepository.existsByCode(456789)).thenReturn(true);

        // Llamar al método
        boolean exists = recoveryCodeRepository.existsByCode(456789);

        // Validar resultados
        assertTrue(exists);

        // Verificar interacción
        verify(recoveryCodeRepository, times(1)).existsByCode(456789);
    }

    @Test
    void testDeleteRecoveryCodeByExpiryDateBefore() {
        LocalDateTime now = LocalDateTime.now();

        // Configurar el mock para un método void
        doNothing().when(recoveryCodeRepository).deleteRecoveryCodeByExpiryDateBefore(now);

        // Llamar al método
        recoveryCodeRepository.deleteRecoveryCodeByExpiryDateBefore(now);

        // Verificar interacción
        verify(recoveryCodeRepository, times(1)).deleteRecoveryCodeByExpiryDateBefore(now);
    }

    @Test
    void testDeleteRecoveryCodesByUserId() {
        // Configurar el mock para un método void
        doNothing().when(recoveryCodeRepository).deleteRecoveryCodesByUserId("123");

        // Llamar al método
        recoveryCodeRepository.deleteRecoveryCodesByUserId("123");

        // Verificar interacción
        verify(recoveryCodeRepository, times(1)).deleteRecoveryCodesByUserId("123");
    }
}
