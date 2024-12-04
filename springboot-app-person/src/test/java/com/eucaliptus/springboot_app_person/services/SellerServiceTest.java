package com.eucaliptus.springboot_app_person.services;

import com.eucaliptus.springboot_app_person.enums.EnumRole;
import com.eucaliptus.springboot_app_person.model.Seller;
import com.eucaliptus.springboot_app_person.repository.SellerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SellerServiceTest {

    @Mock
    private SellerRepository sellerRepository;

    @InjectMocks
    private SellerService sellerService;

    private Seller defaultSeller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        defaultSeller = new Seller();
        defaultSeller.setIdNumber("123456");
        defaultSeller.setFirstName("Jane");
        defaultSeller.setLastName("Doe");
        defaultSeller.setEmail("jane.doe@example.com");
        defaultSeller.setActive(true);
        defaultSeller.setUsername("jdoe");
        defaultSeller.setRole(EnumRole.ROLE_SELLER);
    }

    @Test
    void testGetAllSellers_ReturnsListOfSellers() {
        // Arrange
        when(sellerRepository.findAll()).thenReturn(List.of(defaultSeller));

        // Act
        List<Seller> sellers = sellerService.getAllSellers();

        // Assert
        assertThat(sellers).hasSize(1);
        verify(sellerRepository, times(1)).findAll();
    }

    @Test
    void testGetAllActiveSellers_ReturnsListOfActiveSellers() {
        // Arrange
        when(sellerRepository.findByActiveTrueAndRole(EnumRole.ROLE_SELLER))
                .thenReturn(List.of(defaultSeller));

        // Act
        List<Seller> activeSellers = sellerService.getAllActiveSellers();

        // Assert
        assertThat(activeSellers).hasSize(1);
        assertThat(activeSellers.get(0).getRole()).isEqualTo(EnumRole.ROLE_SELLER);
        verify(sellerRepository, times(1)).findByActiveTrueAndRole(EnumRole.ROLE_SELLER);
    }

    @Test
    void testGetSellerById_ReturnsSellerWhenExists() {
        // Arrange
        String id = "123456";
        when(sellerRepository.findById(id)).thenReturn(Optional.of(defaultSeller));

        // Act
        Optional<Seller> seller = sellerService.getSellerById(id);

        // Assert
        assertThat(seller).isPresent();
        assertThat(seller.get().getFirstName()).isEqualTo("Jane");
        verify(sellerRepository, times(1)).findById(id);
    }

    @Test
    void testGetSellerByPersonId_ReturnsSellerWhenExists() {
        // Arrange
        String personId = "123456";
        when(sellerRepository.findByIdNumber(personId)).thenReturn(Optional.of(defaultSeller));

        // Act
        Optional<Seller> seller = sellerService.getSellerByPersonId(personId);

        // Assert
        assertThat(seller).isPresent();
        assertThat(seller.get().getUsername()).isEqualTo("jdoe");
        verify(sellerRepository, times(1)).findByIdNumber(personId);
    }

    @Test
    void testGetSellerByUsername_ReturnsSellerWhenExists() {
        // Arrange
        String username = "jdoe";
        when(sellerRepository.getByUsername(username)).thenReturn(Optional.of(defaultSeller));

        // Act
        Optional<Seller> seller = sellerService.getSellerByUsername(username);

        // Assert
        assertThat(seller).isPresent();
        assertThat(seller.get().getEmail()).isEqualTo("jane.doe@example.com");
        verify(sellerRepository, times(1)).getByUsername(username);
    }

    @Test
    void testSaveSeller_SavesAndReturnsSeller() {
        // Arrange
        when(sellerRepository.save(defaultSeller)).thenReturn(defaultSeller);

        // Act
        Seller savedSeller = sellerService.saveSeller(defaultSeller);

        // Assert
        assertThat(savedSeller.getIdNumber()).isEqualTo("123456");
        verify(sellerRepository, times(1)).save(defaultSeller);
    }

    @Test
    void testUpdateSeller_UpdatesAndReturnsSellerWhenExists() {
        // Arrange
        String id = "123456";
        Seller updatedSeller = new Seller();
        updatedSeller.setFirstName("Updated");
        updatedSeller.setLastName("Doe");
        updatedSeller.setEmail("updated.doe@example.com");

        when(sellerRepository.findByIdNumber(id)).thenReturn(Optional.of(defaultSeller));
        when(sellerRepository.save(any(Seller.class))).thenReturn(defaultSeller);

        // Act
        Optional<Seller> result = sellerService.updateSeller(id, updatedSeller);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getFirstName()).isEqualTo("Updated");
        verify(sellerRepository, times(1)).findByIdNumber(id);
        verify(sellerRepository, times(1)).save(defaultSeller);
    }

    @Test
    void testExistsById_ReturnsTrueWhenSellerExists() {
        // Arrange
        String id = "123456";
        when(sellerRepository.existsByIdNumber(id)).thenReturn(true);

        // Act
        boolean exists = sellerService.existsById(id);

        // Assert
        assertThat(exists).isTrue();
        verify(sellerRepository, times(1)).existsByIdNumber(id);
    }

    @Test
    void testExistsByUsername_ReturnsTrueWhenUsernameExists() {
        // Arrange
        String username = "jdoe";
        when(sellerRepository.existsByUsername(username)).thenReturn(true);

        // Act
        boolean exists = sellerService.existsByUsername(username);

        // Assert
        assertThat(exists).isTrue();
        verify(sellerRepository, times(1)).existsByUsername(username);
    }

    @Test
    void testDeleteSeller_DeactivatesSellerWhenExists() {
        // Arrange
        String id = "123456";
        when(sellerRepository.findByIdNumber(id)).thenReturn(Optional.of(defaultSeller));

        // Act
        boolean result = sellerService.deleteSeller(id, "someToken");

        // Assert
        assertThat(result).isTrue();
        assertThat(defaultSeller.isActive()).isFalse();
        verify(sellerRepository, times(1)).findByIdNumber(id);
        verify(sellerRepository, times(1)).save(defaultSeller);
    }
}
