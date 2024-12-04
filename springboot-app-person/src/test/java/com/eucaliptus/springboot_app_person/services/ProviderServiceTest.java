package com.eucaliptus.springboot_app_person.services;

import com.eucaliptus.springboot_app_person.model.Provider;
import com.eucaliptus.springboot_app_person.repository.ProviderRepository;
import com.eucaliptus.springboot_app_person.utlities.ServicesUri;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProviderServiceTest {

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private APIService apiService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ProviderService providerService;

    private Provider defaultProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        defaultProvider = new Provider();
        defaultProvider.setIdNumber("123456");
        defaultProvider.setFirstName("John");
        defaultProvider.setLastName("Doe");
        defaultProvider.setEmail("john.doe@example.com");
        defaultProvider.setActive(true);
    }

    @Test
    void testGetAllProviders_ReturnsListOfProviders() {
        // Arrange
        when(providerRepository.findAll()).thenReturn(List.of(defaultProvider));

        // Act
        List<Provider> providers = providerService.getAllProviders();

        // Assert
        assertThat(providers).hasSize(1);
        verify(providerRepository, times(1)).findAll();
    }

    @Test
    void testGetProviderById_ReturnsProviderWhenExists() {
        // Arrange
        String id = "123456";
        when(providerRepository.findByIdNumber(id)).thenReturn(Optional.of(defaultProvider));

        // Act
        Optional<Provider> provider = providerService.getProviderById(id);

        // Assert
        assertThat(provider).isPresent();
        assertThat(provider.get().getFirstName()).isEqualTo("John");
        verify(providerRepository, times(1)).findByIdNumber(id);
    }

    @Test
    void testSaveProvider_SavesAndReturnsProvider() {
        // Arrange
        when(providerRepository.save(defaultProvider)).thenReturn(defaultProvider);

        // Act
        Provider savedProvider = providerService.saveProvider(defaultProvider);

        // Assert
        assertThat(savedProvider.getIdNumber()).isEqualTo("123456");
        verify(providerRepository, times(1)).save(defaultProvider);
    }

    @Test
    void testDeleteProvider_SetsActiveFalse() {
        // Arrange
        String id = "123456";
        when(providerRepository.findByIdNumber(id)).thenReturn(Optional.of(defaultProvider));

        // Act
        boolean result = providerService.deleteProvider(id);

        // Assert
        assertThat(result).isTrue();
        assertThat(defaultProvider.isActive()).isFalse();
        verify(providerRepository, times(1)).findByIdNumber(id);
        verify(providerRepository, times(1)).save(defaultProvider);
    }

    @Test
    void testDeleteProviderAndProducts_DeletesProviderAndProductsSuccessfully() {
        // Arrange
        String idProvider = "123456";
        String token = "Bearer testToken";
        when(providerRepository.findByIdNumber(idProvider)).thenReturn(Optional.of(defaultProvider));
        when(apiService.getHeader(token)).thenReturn(null);
        when(restTemplate.exchange(
                eq(ServicesUri.PRODUCT_SERVICE + "/products/deleteProductsByProvider/" + idProvider),
                eq(HttpMethod.DELETE),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(ResponseEntity.ok("Success"));

        // Act
        boolean result = providerService.deleteProviderAndProducts(idProvider, token);

        // Assert
        assertThat(result).isTrue();
        verify(providerRepository, times(1)).findByIdNumber(idProvider);
        verify(providerRepository, times(1)).save(defaultProvider);
        verify(restTemplate, times(1)).exchange(
                eq(ServicesUri.PRODUCT_SERVICE + "/products/deleteProductsByProvider/" + idProvider),
                eq(HttpMethod.DELETE),
                any(HttpEntity.class),
                eq(String.class)
        );
    }

    @Test
    void testDeleteProviderAndProducts_ThrowsExceptionWhenProviderNotDeleted() {
        // Arrange
        String idProvider = "123456";
        String token = "Bearer testToken";
        when(providerRepository.findByIdNumber(idProvider)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> providerService.deleteProviderAndProducts(idProvider, token));
        verify(providerRepository, times(1)).findByIdNumber(idProvider);
        verify(restTemplate, never()).exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(String.class));
    }
}
