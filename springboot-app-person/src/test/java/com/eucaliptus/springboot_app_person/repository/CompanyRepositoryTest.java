package com.eucaliptus.springboot_app_person.repository;

import com.eucaliptus.springboot_app_person.model.Company;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CompanyRepositoryTest {

    @Mock
    private CompanyRepository companyRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExistsByNitCompany_ReturnsTrueWhenExists() {
        // Arrange
        String nit = "123456789";
        when(companyRepository.existsByNitCompany(nit)).thenReturn(true);

        // Act
        boolean exists = companyRepository.existsByNitCompany(nit);

        // Assert
        assertThat(exists).isTrue();
        verify(companyRepository, times(1)).existsByNitCompany(nit);
    }

    @Test
    void testExistsByNitCompany_ReturnsFalseWhenNotExists() {
        // Arrange
        String nit = "987654321";
        when(companyRepository.existsByNitCompany(nit)).thenReturn(false);

        // Act
        boolean exists = companyRepository.existsByNitCompany(nit);

        // Assert
        assertThat(exists).isFalse();
        verify(companyRepository, times(1)).existsByNitCompany(nit);
    }

    @Test
    void testSaveCompany() {
        // Arrange
        Company company = new Company("123456789", "Eucaliptus SAS", "contact@eucaliptus.com", "3200000000", "Calle 123 #45-67");
        when(companyRepository.save(company)).thenReturn(company);

        // Act
        Company savedCompany = companyRepository.save(company);

        // Assert
        assertThat(savedCompany).isNotNull();
        assertThat(savedCompany.getNameCompany()).isEqualTo("Eucaliptus SAS");
        verify(companyRepository, times(1)).save(company);
    }

    @Test
    void testFindById_ReturnsCompanyWhenExists() {
        // Arrange
        String nit = "123456789";
        Company company = new Company(nit, "Eucaliptus SAS", "contact@eucaliptus.com", "3200000000", "Calle 123 #45-67");
        when(companyRepository.findById(nit)).thenReturn(Optional.of(company));

        // Act
        Optional<Company> foundCompany = companyRepository.findById(nit);

        // Assert
        assertThat(foundCompany).isPresent();
        assertThat(foundCompany.get().getNameCompany()).isEqualTo("Eucaliptus SAS");
        verify(companyRepository, times(1)).findById(nit);
    }

    @Test
    void testFindById_ReturnsEmptyWhenNotExists() {
        // Arrange
        String nit = "987654321";
        when(companyRepository.findById(nit)).thenReturn(Optional.empty());

        // Act
        Optional<Company> foundCompany = companyRepository.findById(nit);

        // Assert
        assertThat(foundCompany).isNotPresent();
        verify(companyRepository, times(1)).findById(nit);
    }
}
