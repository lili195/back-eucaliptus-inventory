package com.eucaliptus.springboot_app_billing.repository;

import com.eucaliptus.springboot_app_billing.model.Purchase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PurchaseRepositoryTest {

    @Mock
    private PurchaseRepository purchaseRepository;

    private Purchase purchase1, purchase2;

    @BeforeEach
    public void setUp() {
        // Inicializar datos de prueba
        purchase1 = new Purchase();
        purchase1.setIdPurchase(1);
        purchase1.setIdProvider("Provider1");
        purchase1.setPurchaseDate(new Date());
        purchase1.setTotalPurchase(150.0);

        purchase2 = new Purchase();
        purchase2.setIdPurchase(2);
        purchase2.setIdProvider("Provider2");
        purchase2.setPurchaseDate(new Date());
        purchase2.setTotalPurchase(250.0);
    }

    @Test
    public void testFindByPurchaseDate() {
        // Simula la llamada al repositorio
        when(purchaseRepository.findByPurchaseDate(purchase1.getPurchaseDate())).thenReturn(Arrays.asList(purchase1, purchase2));

        // Llama al método del repositorio y verifica los resultados
        List<Purchase> result = purchaseRepository.findByPurchaseDate(purchase1.getPurchaseDate());
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Provider1", result.get(0).getIdProvider());
    }
}
