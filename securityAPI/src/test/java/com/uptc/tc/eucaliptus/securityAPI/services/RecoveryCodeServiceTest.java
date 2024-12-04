package com.uptc.tc.eucaliptus.securityAPI.services;

import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.RecoveryCode;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.User;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.repositories.RecoveryCodeRepository;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.services.RecoveryCodeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecoveryCodeServiceTest {

    @Mock
    private RecoveryCodeRepository recoveryCodeRepository;

    @InjectMocks
    private RecoveryCodeService recoveryCodeService;

    @Test
    public void testSaveNewRecoveryCode() {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);

        RecoveryCode recoveryCode = new RecoveryCode();
        recoveryCode.setCode(123456);
        recoveryCode.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        recoveryCode.setUser(user);

        when(recoveryCodeRepository.save(recoveryCode)).thenReturn(recoveryCode);
        RecoveryCode savedRecoveryCode = recoveryCodeService.saveNewRecoveryCode(recoveryCode);

        assertNotNull(savedRecoveryCode);
        assertEquals(123456, savedRecoveryCode.getCode());
        verify(recoveryCodeRepository, times(1)).save(recoveryCode);
    }

    @Test
    public void testFindByCode_CodeFound() {
        RecoveryCode recoveryCode = new RecoveryCode();
        recoveryCode.setCode(123456);
        when(recoveryCodeRepository.findByCode(123456)).thenReturn(Optional.of(recoveryCode));
        Optional<RecoveryCode> foundCode = recoveryCodeService.findByCode(123456);

        assertTrue(foundCode.isPresent());
        assertEquals(123456, foundCode.get().getCode());
        verify(recoveryCodeRepository, times(1)).findByCode(123456);
    }

    @Test
    public void testFindByCode_CodeNotFound() {
        when(recoveryCodeRepository.findByCode(999999)).thenReturn(Optional.empty());
        Optional<RecoveryCode> foundCode = recoveryCodeService.findByCode(999999);

        assertFalse(foundCode.isPresent());
        verify(recoveryCodeRepository, times(1)).findByCode(999999);
    }

    @Test
    public void testDeleteExpiratedCodes() {
        recoveryCodeService.deleteExpiratedCodes();

        verify(recoveryCodeRepository, times(0)).deleteRecoveryCodeByExpiryDateBefore(LocalDateTime.now());
    }


}
