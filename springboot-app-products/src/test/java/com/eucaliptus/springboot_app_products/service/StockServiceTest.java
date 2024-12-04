package com.eucaliptus.springboot_app_products.service;

import com.eucaliptus.springboot_app_products.dto.NewBatchDTO;
import com.eucaliptus.springboot_app_products.model.Product;
import com.eucaliptus.springboot_app_products.model.Stock;
import com.eucaliptus.springboot_app_products.repository.StockRepository;
import com.eucaliptus.springboot_app_products.mappers.StockMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private ProductService productService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private StockService stockService;

    private Stock stock;
    private Product product;
    private NewBatchDTO newBatchDTO;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setIdProduct("P001");
        product.setProductName("Product 1");

        stock = new Stock();
        stock.setProduct(product);
        stock.setQuantityAvailable(50);

        newBatchDTO = new NewBatchDTO();
        newBatchDTO.setIdProduct("P001");
        newBatchDTO.setQuantityPurchased(100);
    }

    @Test
    void testGetStocksAvailable() {
        List<Stock> stocks = Arrays.asList(stock);
        when(stockRepository.findLatestStockByProduct()).thenReturn(stocks);

        List<Stock> result = stockService.getStocksAvailable();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(stockRepository, times(1)).findLatestStockByProduct();
    }

    @Test
    void testGetStockByProductId() {
        when(stockRepository.findFirstByProduct_IdProductOrderByModificationDateStockDesc("P001")).thenReturn(Optional.of(stock));

        Optional<Stock> result = stockService.getStockByProductId("P001");

        assertTrue(result.isPresent());
        assertEquals(product.getIdProduct(), result.get().getProduct().getIdProduct());
        verify(stockRepository, times(1)).findFirstByProduct_IdProductOrderByModificationDateStockDesc("P001");
    }

    @Test
    void testSaveStock() {
        when(stockRepository.save(stock)).thenReturn(stock);

        Stock savedStock = stockService.saveStock(stock);

        assertNotNull(savedStock);
        assertEquals(stock.getQuantityAvailable(), savedStock.getQuantityAvailable());
        verify(stockRepository, times(1)).save(stock);
    }

    @Test
    void testUpdateStocks() {
        List<NewBatchDTO> batches = Arrays.asList(newBatchDTO);

        when(productService.getProductById("P001")).thenReturn(Optional.of(product));
        when(stockRepository.findFirstByProduct_IdProductOrderByModificationDateStockDesc("P001")).thenReturn(Optional.empty());
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        boolean result = stockService.updateStocks(batches);

        assertTrue(result);
        verify(productService, times(1)).getProductById("P001");
        verify(stockRepository, times(1)).findFirstByProduct_IdProductOrderByModificationDateStockDesc("P001");
        verify(stockRepository, times(1)).save(any(Stock.class));
    }

    @Test
    void testUpdateStocks_ProductNotFound() {
        List<NewBatchDTO> batches = Arrays.asList(newBatchDTO);

        when(productService.getProductById("P001")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            stockService.updateStocks(batches);
        });

        assertEquals("Producto no encontrado: P001", exception.getMessage());
        verify(productService, times(1)).getProductById("P001");
        verify(stockRepository, never()).save(any(Stock.class));
    }
}
