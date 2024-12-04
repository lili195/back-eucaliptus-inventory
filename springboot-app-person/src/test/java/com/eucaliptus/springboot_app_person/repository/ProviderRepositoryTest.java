package com.eucaliptus.springboot_app_person.repository;

import com.eucaliptus.springboot_app_person.enums.EnumDocumentType;
import com.eucaliptus.springboot_app_person.enums.EnumPersonType;
import com.eucaliptus.springboot_app_person.enums.EnumRole;
import com.eucaliptus.springboot_app_person.model.Company;
import com.eucaliptus.springboot_app_person.model.DocumentType;
import com.eucaliptus.springboot_app_person.model.Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProviderRepositoryTest {

    @Mock
    private ProviderRepository providerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExistsByIdNumber_ReturnsTrueWhenExists() {
        // Arrange
        String idNumber = "123456";
        when(providerRepository.existsByIdNumber(idNumber)).thenReturn(true);

        // Act
        boolean exists = providerRepository.existsByIdNumber(idNumber);

        // Assert
        assertThat(exists).isTrue();
        verify(providerRepository, times(1)).existsByIdNumber(idNumber);
    }

    @Test
    void testExistsByIdNumber_ReturnsFalseWhenNotExists() {
        // Arrange
        String idNumber = "654321";
        when(providerRepository.existsByIdNumber(idNumber)).thenReturn(false);

        // Act
        boolean exists = providerRepository.existsByIdNumber(idNumber);

        // Assert
        assertThat(exists).isFalse();
        verify(providerRepository, times(1)).existsByIdNumber(idNumber);
    }

    @Test
    void testFindByActiveTrue_ReturnsActiveProviders() {
        // Arrange
        Provider provider1 = new Provider("123456", "John", "Doe", "john.doe@example.com", "123 Street", "1234567890", new DocumentType(EnumDocumentType.CC));
        provider1.setActive(true);
        Provider provider2 = new Provider("789012", "Jane", "Smith", "jane.smith@example.com", "456 Avenue", "0987654321", new DocumentType(EnumDocumentType.CE));
        provider2.setActive(true);
        when(providerRepository.findByActiveTrue()).thenReturn(List.of(provider1, provider2));

        // Act
        List<Provider> activeProviders = providerRepository.findByActiveTrue();

        // Assert
        assertThat(activeProviders).isNotEmpty();
        assertThat(activeProviders).hasSize(2);
        verify(providerRepository, times(1)).findByActiveTrue();
    }

    @Test
    void testFindByIdNumber_ReturnsProviderWhenExists() {
        // Arrange
        String idNumber = "123456";
        Provider provider = new Provider(idNumber, "John", "Doe", "john.doe@example.com", "123 Street", "1234567890", new DocumentType(EnumDocumentType.CC));
        when(providerRepository.findByIdNumber(idNumber)).thenReturn(Optional.of(provider));

        // Act
        Optional<Provider> foundProvider = providerRepository.findByIdNumber(idNumber);

        // Assert
        assertThat(foundProvider).isPresent();
        assertThat(foundProvider.get().getIdNumber()).isEqualTo(idNumber);
        verify(providerRepository, times(1)).findByIdNumber(idNumber);
    }

    @Test
    void testFindByIdNumber_ReturnsEmptyWhenNotExists() {
        // Arrange
        String idNumber = "654321";
        when(providerRepository.findByIdNumber(idNumber)).thenReturn(Optional.empty());

        // Act
        Optional<Provider> foundProvider = providerRepository.findByIdNumber(idNumber);

        // Assert
        assertThat(foundProvider).isNotPresent();
        verify(providerRepository, times(1)).findByIdNumber(idNumber);
    }

    @Test
    void testFindByActiveTrueAndCompany_NitCompany_ReturnsProvider() {
        // Arrange
        String companyId = "111222333";
        Company company = new Company(companyId, "TechCorp", "contact@techcorp.com", "1234567890", "123 Business Street");
        Provider provider = new Provider("123456", "John", "Doe", "john.doe@example.com", "123 Street", "1234567890", new DocumentType(EnumDocumentType.CC));
        provider.setActive(true);
        provider.setCompany(company);
        when(providerRepository.findByActiveTrueAndCompany_NitCompany(companyId)).thenReturn(Optional.of(provider));

        // Act
        Optional<Provider> foundProvider = providerRepository.findByActiveTrueAndCompany_NitCompany(companyId);

        // Assert
        assertThat(foundProvider).isPresent();
        assertThat(foundProvider.get().getCompany().getNitCompany()).isEqualTo(companyId);
        verify(providerRepository, times(1)).findByActiveTrueAndCompany_NitCompany(companyId);
    }

    @Test
    void testFindByActiveTrueAndCompany_NitCompany_ReturnsEmptyWhenNotExists() {
        // Arrange
        String companyId = "999888777";
        when(providerRepository.findByActiveTrueAndCompany_NitCompany(companyId)).thenReturn(Optional.empty());

        // Act
        Optional<Provider> foundProvider = providerRepository.findByActiveTrueAndCompany_NitCompany(companyId);

        // Assert
        assertThat(foundProvider).isNotPresent();
        verify(providerRepository, times(1)).findByActiveTrueAndCompany_NitCompany(companyId);
    }
}
