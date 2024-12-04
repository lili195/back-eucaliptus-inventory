package com.eucaliptus.springboot_app_billing.repository;

import com.eucaliptus.springboot_app_billing.model.Client;
import com.eucaliptus.springboot_app_billing.model.Sale;
import com.eucaliptus.springboot_app_billing.service.SaleService;
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
public class SaleRepositoryTest {

    @Mock
    private SaleRepository saleRepository;

    private Client client;
    private Sale sale1, sale2;

    @BeforeEach
    public void setUp() {
        // Inicializar datos de prueba
        client = new Client();
        client.setIdClient("123");
        client.setNameClient("Test Client");

        sale1 = new Sale();
        sale1.setIdSale(1);
        sale1.setClient(client);
        sale1.setSaleDate(new Date());
        sale1.setTotalSale(100.0);

        sale2 = new Sale();
        sale2.setIdSale(2);
        sale2.setClient(client);
        sale2.setSaleDate(new Date());
        sale2.setTotalSale(200.0);
    }

    @Test
    public void testFindBySaleDate() {
        when(saleRepository.findBySaleDate(sale1.getSaleDate())).thenReturn(Arrays.asList(sale1, sale2));

        List<Sale> result = saleRepository.findBySaleDate(sale1.getSaleDate());
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByClientId() {
        when(saleRepository.findByClient_IdClient("123")).thenReturn(Arrays.asList(sale1, sale2));

        List<Sale> result = saleRepository.findByClient_IdClient("123");
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testExistsByClientIdAndSaleDate() {
        when(saleRepository.existsByClient_IdClientAndSaleDate("123", sale1.getSaleDate())).thenReturn(true);

        boolean exists = saleRepository.existsByClient_IdClientAndSaleDate("123", sale1.getSaleDate());
        assertTrue(exists);
    }

    @Test
    public void testFindByTotalSaleGreaterThan() {
        when(saleRepository.findByTotalSaleGreaterThan(150.0)).thenReturn(Arrays.asList(sale2));

        List<Sale> result = saleRepository.findByTotalSaleGreaterThan(150.0);
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
