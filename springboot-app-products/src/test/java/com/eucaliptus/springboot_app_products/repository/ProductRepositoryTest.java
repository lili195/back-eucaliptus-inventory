package com.eucaliptus.springboot_app_products.repository;

import java.util.List;
import com.eucaliptus.springboot_app_products.model.Product;
import com.eucaliptus.springboot_app_products.model.Unit;
import com.eucaliptus.springboot_app_products.enums.EnumCategory;
import com.eucaliptus.springboot_app_products.enums.EnumUse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductRepositoryTest {

    @Mock
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        Unit unit = new Unit("Kilogram", "Weight measurement unit");
        product = new Product("P001", "Product 1", "Brand A", "PERECEDERO", "SUPLEMENTOS", "Provider 1", "Description", unit, 10, 100);
    }

    @Test
    void testExistsByIdProduct() {
        when(productRepository.existsByIdProduct("P001")).thenReturn(true);

        boolean exists = productRepository.existsByIdProduct("P001");

        assertTrue(exists);
        verify(productRepository, times(1)).existsByIdProduct("P001");
    }

    @Test
    void testFindByIdProduct() {
        when(productRepository.findByIdProduct("P001")).thenReturn(Optional.of(product));

        Optional<Product> foundProduct = productRepository.findByIdProduct("P001");

        assertTrue(foundProduct.isPresent());
        assertEquals("P001", foundProduct.get().getIdProduct());
        verify(productRepository, times(1)).findByIdProduct("P001");
    }

    @Test
    void testFindByActiveTrue() {
        when(productRepository.findByActiveTrue()).thenReturn(List.of(product));

        List<Product> activeProducts = productRepository.findByActiveTrue();

        assertNotNull(activeProducts);
        assertFalse(activeProducts.isEmpty());
        verify(productRepository, times(1)).findByActiveTrue();
    }

    @Test
    void testFindByIdProviderAndActiveTrue() {
        when(productRepository.findByIdProviderAndActiveTrue("Provider 1")).thenReturn(List.of(product));

        List<Product> products = productRepository.findByIdProviderAndActiveTrue("Provider 1");

        assertNotNull(products);
        assertFalse(products.isEmpty());
        assertEquals("Provider 1", products.get(0).getIdProvider());
        verify(productRepository, times(1)).findByIdProviderAndActiveTrue("Provider 1");
    }

    @Test
    void testExistsByProductName() {
        when(productRepository.existsByProductName("Product 1")).thenReturn(true);

        boolean exists = productRepository.existsByProductName("Product 1");

        assertTrue(exists);
        verify(productRepository, times(1)).existsByProductName("Product 1");
    }

    @Test
    void testFindByProductName() {
        when(productRepository.findByProductName("Product 1")).thenReturn(Optional.of(product));

        Optional<Product> foundProduct = productRepository.findByProductName("Product 1");

        assertTrue(foundProduct.isPresent());
        assertEquals("Product 1", foundProduct.get().getProductName());
        verify(productRepository, times(1)).findByProductName("Product 1");
    }
}
