package com.eucaliptus.springboot_app_products.repository;

import com.eucaliptus.springboot_app_products.model.Product;
import com.eucaliptus.springboot_app_products.model.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockRepositoryTest {

    @Mock
    private StockRepository stockRepository;

    private Product product;
    private Stock stock;

    @BeforeEach
    void setUp() {
        product = new Product("P001", "Product 1", "Brand A", "PERECEDERO", "SUPLEMENTOS", "Provider 1", "Description", null, 10, 100);
        stock = new Stock(product, 50);
        stock.setModificationDateStock(new Date());
    }

    @Test
    void testFindFirstByProduct_IdProductOrderByModificationDateStockDesc() {
        when(stockRepository.findFirstByProduct_IdProductOrderByModificationDateStockDesc("P001"))
                .thenReturn(Optional.of(stock));

        Optional<Stock> foundStock = stockRepository.findFirstByProduct_IdProductOrderByModificationDateStockDesc("P001");

        assertTrue(foundStock.isPresent());
        assertEquals("P001", foundStock.get().getProduct().getIdProduct());
        verify(stockRepository, times(1)).findFirstByProduct_IdProductOrderByModificationDateStockDesc("P001");
    }

    @Test
    void testFindLatestStockByProduct() {
        when(stockRepository.findLatestStockByProduct()).thenReturn(List.of(stock));

        List<Stock> latestStock = stockRepository.findLatestStockByProduct();

        assertNotNull(latestStock);
        assertFalse(latestStock.isEmpty());
        assertEquals("P001", latestStock.get(0).getProduct().getIdProduct());
        verify(stockRepository, times(1)).findLatestStockByProduct();
    }
}
