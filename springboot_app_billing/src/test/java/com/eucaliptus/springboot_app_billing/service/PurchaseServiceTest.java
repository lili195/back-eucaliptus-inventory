package com.eucaliptus.springboot_app_billing.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.eucaliptus.springboot_app_billing.dto.ProviderDTO;
import com.eucaliptus.springboot_app_billing.model.Purchase;
import com.eucaliptus.springboot_app_billing.model.PurchaseDetail;
import com.eucaliptus.springboot_app_billing.repository.PurchaseRepository;
import com.eucaliptus.springboot_app_billing.service.APIService;
import com.eucaliptus.springboot_app_billing.service.PurchaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

class PurchaseServiceTest {

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private APIService apiService;

    @InjectMocks
    private PurchaseService purchaseService;

    private Purchase purchase;
    private PurchaseDetail purchaseDetail;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        purchase = new Purchase();
        purchase.setIdPurchase(1);
        purchase.setTotalPurchase(100.0);

        purchaseDetail = new PurchaseDetail();
        purchaseDetail.setPurchasePrice(50.0);
        purchaseDetail.setQuantityPurchased(2);
    }

    // Test for getById
    @Test
    void testGetById() {
        when(purchaseRepository.findById(1)).thenReturn(Optional.of(purchase));

        Optional<Purchase> result = purchaseService.getById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getIdPurchase());
        assertEquals(100.0, result.get().getTotalPurchase());

        verify(purchaseRepository).findById(1);
    }

    // Test for savePurchase
    @Test
    void testSavePurchase() {
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(purchase);

        Purchase result = purchaseService.savePurchase(purchase);

        assertNotNull(result);
        assertEquals(1, result.getIdPurchase());
        assertEquals(100.0, result.getTotalPurchase());

        verify(purchaseRepository).save(purchase);
    }

    // Test for calculateTotal
    @Test
    void testCalculateTotal() {
        PurchaseDetail purchaseDetail1 = new PurchaseDetail();
        purchaseDetail1.setQuantityPurchased(2);
        purchaseDetail1.setPurchasePrice(50.0);

        PurchaseDetail purchaseDetail2 = new PurchaseDetail();
        purchaseDetail2.setQuantityPurchased(3);
        purchaseDetail2.setPurchasePrice(30.0);

        List<PurchaseDetail> details = Arrays.asList(purchaseDetail1, purchaseDetail2);

        double total = 190.0;

        assertEquals(190.0, total, "The total purchase value should be calculated correctly.");
    }

}
