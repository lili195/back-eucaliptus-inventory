package com.eucaliptus.springboot_app_products.controller;

import com.eucaliptus.springboot_app_products.controllers.ProductController;
import com.eucaliptus.springboot_app_products.dto.Message;
import com.eucaliptus.springboot_app_products.dto.ProductDTO;
import com.eucaliptus.springboot_app_products.model.Product;
import com.eucaliptus.springboot_app_products.service.APIService;
import com.eucaliptus.springboot_app_products.service.ProductService;
import com.eucaliptus.springboot_app_products.service.UnitService;
import com.eucaliptus.springboot_app_products.mappers.ProductMapper;
import com.eucaliptus.springboot_app_products.model.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @Mock
    private UnitService unitService;

    @Mock
    private APIService apiService;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts_Success() {
        when(productService.getAllActiveProducts()).thenReturn(new ArrayList<>());

        ResponseEntity<Object> response = productController.getAllProducts();

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetProductById_ProductNotFound() {
        String productId = "123";
        when(productService.existsByIdProduct(productId)).thenReturn(false);

        ResponseEntity<Object> response = productController.getProductById(productId);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Producto no encontrado", ((Message) response.getBody()).getMessage());
    }

    @Test
    void testCreateProduct_Success() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setIdProduct("123");
        productDTO.setIdProvider("provider123");

        when(productService.existsProviderId(anyString(), anyString())).thenReturn(true);
        when(productService.existsByIdProduct(anyString())).thenReturn(false);
        when(unitService.getUnitByNameAndDescription(anyString(), anyString())).thenReturn(Optional.of(new Unit()));

        Product product = new Product();
        when(productService.saveProduct(any(Product.class))).thenReturn(product);

        ResponseEntity<Object> response = productController.createProduct(productDTO, request);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdateProduct_ProductNotFound() {
        String productId = "123";
        ProductDTO productDTO = new ProductDTO();
        when(productService.existsByIdProduct(productId)).thenReturn(false);

        ResponseEntity<Object> response = productController.updateProduct(productId, productDTO, request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Producto no encontrado", ((Message) response.getBody()).getMessage());
    }

    @Test
    void testDeleteProduct_ProductNotFound() {
        String productId = "123";
        when(productService.existsByIdProduct(productId)).thenReturn(false);

        ResponseEntity<Object> response = productController.deleteProduct(productId, request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Producto no encontrado", ((Message) response.getBody()).getMessage());
    }

    @Test
    void testDeleteProductsByProvider_Success() {
        String providerId = "provider123";
        doNothing().when(productService).deleteProductsByIdProvider(providerId);

        ResponseEntity<Object> response = productController.deleteProductsByProvider(providerId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Producto eliminado exitosamente", ((Message) response.getBody()).getMessage());
    }

    @Test
    void testDeleteProductsByProvider_Exception() {
        String providerId = "provider123";
        doThrow(new RuntimeException("Error")).when(productService).deleteProductsByIdProvider(providerId);

        ResponseEntity<Object> response = productController.deleteProductsByProvider(providerId);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Intente de nuevo más tarde", ((Message) response.getBody()).getMessage());
    }

}
