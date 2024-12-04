package com.eucaliptus.springboot_app_billing.controllers;

import com.eucaliptus.springboot_app_billing.dto.*;
import com.eucaliptus.springboot_app_billing.model.Purchase;
import com.eucaliptus.springboot_app_billing.service.*;
import com.eucaliptus.springboot_app_billing.security.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PurchaseControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PurchaseService purchaseService;
    @Mock
    private PurchaseDetailService purchaseDetailService;
    @Mock
    private ProductService productService;
    @Mock
    private APIService apiService;
    @Mock
    private SendBillService sendBillService;
    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private PurchaseController purchaseController;

    private PurchaseDTO purchaseDTO;
    private Purchase purchase;
    private String token = "fake-jwt-token";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(purchaseController).build();
        // Prepare mock data
        purchaseDTO = new PurchaseDTO();
        purchaseDTO.setPurchaseId(1);
        purchaseDTO.setProviderId("provider1");
        purchaseDTO.setTotalPurchase(100.0);
        purchase = new Purchase();
        purchase.setIdPurchase(1);
        purchase.setIdProvider("provider1");
        purchase.setTotalPurchase(100.0);
    }

    @Test
    public void testAddPurchase_Success() throws Exception {
        // Arrange
        when(purchaseService.saveNewPurchase(any(PurchaseDTO.class), any())).thenReturn(purchase);
        when(purchaseDetailService.findByPurchaseId(anyInt())).thenReturn(new ArrayList<>());
        when(productService.getPurchaseDetails(anyList(), anyString())).thenReturn(new ArrayList<>());
        when(purchaseService.getProvider(anyString(), anyString())).thenReturn(new ProviderDTO());

        // Act & Assert
        mockMvc.perform(post("/billing/purchase/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(purchaseDTO))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.purchaseId").value(1));
    }

    @Test
    public void testAddPurchase_Failure() throws Exception {
        // Arrange
        when(purchaseService.saveNewPurchase(any(PurchaseDTO.class), any())).thenThrow(new IllegalArgumentException("Proveedor no encontrado"));

        // Act & Assert
        mockMvc.perform(post("/billing/purchase/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(purchaseDTO))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Proveedor no encontrado"));
    }

    @Test
    public void testGetHistoryPurchase_Success() throws Exception {
        // Arrange
        DatesDTO datesDTO = new DatesDTO(new Date(), new Date());
        datesDTO.setStartDate(new Date());
        when(purchaseService.getPurchasesByDate(any(Date.class))).thenReturn(Collections.singletonList(purchase));

        // Act & Assert
        mockMvc.perform(post("/billing/purchase/getHistoryPurchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(datesDTO))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].purchaseId").value(1));
    }

    @Test
    public void testGetPurchaseDetails_Success() throws Exception {
        // Arrange
        when(purchaseService.getById(anyInt())).thenReturn(Optional.of(purchase));
        when(purchaseDetailService.findByPurchaseId(anyInt())).thenReturn(new ArrayList<>());
        when(productService.getPurchaseDetails(anyList(), anyString())).thenReturn(new ArrayList<>());
        when(purchaseService.getProvider(anyString(), anyString())).thenReturn(new ProviderDTO());

        // Act & Assert
        mockMvc.perform(get("/billing/purchase/getPurchaseDetails/{idPurchase}", 1)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.purchaseId").value(1));
    }

    @Test
    public void testGetPurchaseDetails_NotFound() throws Exception {
        // Arrange
        when(purchaseService.getById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/billing/purchase/getPurchaseDetails/{idPurchase}", 999)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Compra no encontrada"));
    }

    @Test
    public void testSendEmail_NotFound() throws Exception {
        // Arrange
        when(purchaseService.getById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(post("/billing/purchase/sendEmail/{idPurchase}", 999)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Compra no encontrada"));
    }
}
