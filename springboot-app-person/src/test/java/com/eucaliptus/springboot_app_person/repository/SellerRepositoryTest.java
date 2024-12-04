package com.eucaliptus.springboot_app_person.repository;

import com.eucaliptus.springboot_app_person.enums.EnumDocumentType;
import com.eucaliptus.springboot_app_person.enums.EnumRole;
import com.eucaliptus.springboot_app_person.model.DocumentType;
import com.eucaliptus.springboot_app_person.model.Seller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SellerRepositoryTest {

    @Mock
    private SellerRepository sellerRepository;

    private DocumentType defaultDocumentType;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        defaultDocumentType = new DocumentType();
        defaultDocumentType.setIdDocumentType(1L);
        defaultDocumentType.setNameType(EnumDocumentType.CC);
    }

    @Test
    void testExistsByIdNumber_ReturnsTrueWhenExists() {
        // Arrange
        String idNumber = "987654";
        when(sellerRepository.existsByIdNumber(idNumber)).thenReturn(true);

        // Act
        boolean exists = sellerRepository.existsByIdNumber(idNumber);

        // Assert
        assertThat(exists).isTrue();
        verify(sellerRepository, times(1)).existsByIdNumber(idNumber);
    }

    @Test
    void testExistsByUsername_ReturnsTrueWhenExists() {
        // Arrange
        String username = "sellerUser";
        when(sellerRepository.existsByUsername(username)).thenReturn(true);

        // Act
        boolean exists = sellerRepository.existsByUsername(username);

        // Assert
        assertThat(exists).isTrue();
        verify(sellerRepository, times(1)).existsByUsername(username);
    }

    @Test
    void testFindByActiveTrueAndRole_ReturnsSellersWithRole() {
        // Arrange
        Seller seller1 = new Seller("123456", "John", "Doe", "john.doe@example.com",
                "123 Main St", "123456789", defaultDocumentType, "seller1");
        Seller seller2 = new Seller("789012", "Jane", "Smith", "jane.smith@example.com",
                "456 Elm St", "987654321", defaultDocumentType, "seller2");
        seller1.setActive(true);
        seller2.setActive(true);

        when(sellerRepository.findByActiveTrueAndRole(EnumRole.ROLE_SELLER)).thenReturn(List.of(seller1, seller2));

        // Act
        List<Seller> sellers = sellerRepository.findByActiveTrueAndRole(EnumRole.ROLE_SELLER);

        // Assert
        assertThat(sellers).hasSize(2);
        assertThat(sellers.get(0).getUsername()).isEqualTo("seller1");
        assertThat(sellers.get(1).getUsername()).isEqualTo("seller2");
        verify(sellerRepository, times(1)).findByActiveTrueAndRole(EnumRole.ROLE_SELLER);
    }

    @Test
    void testFindByIdNumber_ReturnsSellerWhenExists() {
        // Arrange
        String idNumber = "987654";
        Seller seller = new Seller(idNumber, "John", "Doe", "john.doe@example.com",
                "123 Main St", "123456789", defaultDocumentType, "sellerUser");
        when(sellerRepository.findByIdNumber(idNumber)).thenReturn(Optional.of(seller));

        // Act
        Optional<Seller> foundSeller = sellerRepository.findByIdNumber(idNumber);

        // Assert
        assertThat(foundSeller).isPresent();
        assertThat(foundSeller.get().getUsername()).isEqualTo("sellerUser");
        verify(sellerRepository, times(1)).findByIdNumber(idNumber);
    }

    @Test
    void testGetByUsername_ReturnsSellerWhenExists() {
        // Arrange
        String username = "sellerUser";
        Seller seller = new Seller("123456", "John", "Doe", "john.doe@example.com",
                "123 Main St", "123456789", defaultDocumentType, username);
        when(sellerRepository.getByUsername(username)).thenReturn(Optional.of(seller));

        // Act
        Optional<Seller> foundSeller = sellerRepository.getByUsername(username);

        // Assert
        assertThat(foundSeller).isPresent();
        assertThat(foundSeller.get().getUsername()).isEqualTo(username);
        verify(sellerRepository, times(1)).getByUsername(username);
    }
}
