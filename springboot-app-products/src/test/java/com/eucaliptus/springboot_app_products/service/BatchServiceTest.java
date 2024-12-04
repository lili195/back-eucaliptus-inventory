package com.eucaliptus.springboot_app_products.service;

import com.eucaliptus.springboot_app_products.dto.Message;
import com.eucaliptus.springboot_app_products.dto.NewBatchDTO;
import com.eucaliptus.springboot_app_products.model.Batch;
import com.eucaliptus.springboot_app_products.repository.BatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BatchServiceTest {

    @Mock
    private BatchRepository batchRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private BatchService batchService;

    private Batch batch;
    private NewBatchDTO newBatchDTO;

    @BeforeEach
    void setUp() {
        batch = new Batch();
        batch.setIdProduct("P001");
        batch.setBatch(new Date());
        batch.setQuantityAvailableBatch(100);
        batch.setDueDate(new Date());

        newBatchDTO = new NewBatchDTO();
        newBatchDTO.setIdProduct("P001");
        newBatchDTO.setBatchPurchase(new Date());
        newBatchDTO.setQuantityPurchased(100);
        newBatchDTO.setPurchaseDueDate(new Date());
    }

    @Test
    void testGetAllBatches() {
        List<Batch> batches = Arrays.asList(batch);
        when(batchRepository.findAll()).thenReturn(batches);

        List<Batch> result = batchService.getAllBatches();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(batchRepository, times(1)).findAll();
    }

    @Test
    void testSaveBatch() {
        when(batchRepository.save(batch)).thenReturn(batch);

        Batch savedBatch = batchService.saveBatch(batch);

        assertNotNull(savedBatch);
        assertEquals(batch.getIdProduct(), savedBatch.getIdProduct());
        verify(batchRepository, times(1)).save(batch);
    }

    @Test
    void testUpdateQuantityAvailable() {
        when(batchRepository.findByIdProductAndBatch("P001", batch.getBatch())).thenReturn(Optional.of(batch));
        when(batchRepository.save(batch)).thenReturn(batch);

        Optional<Batch> updatedBatch = batchService.updateQuantityAvailable("P001", batch.getBatch(), 200);

        assertTrue(updatedBatch.isPresent());
        assertEquals(200, updatedBatch.get().getQuantityAvailableBatch());
        verify(batchRepository, times(1)).findByIdProductAndBatch("P001", batch.getBatch());
        verify(batchRepository, times(1)).save(batch);
    }

    @Test
    void testAddBatches() {
        List<NewBatchDTO> batches = Arrays.asList(newBatchDTO);

        when(productService.existsByIdProduct("P001")).thenReturn(true);
        when(batchRepository.existsByIdProductAndBatch("P001", newBatchDTO.getBatchPurchase())).thenReturn(false);
        when(batchRepository.save(any(Batch.class))).thenReturn(batch);

        boolean result = batchService.addBatches(batches);

        assertTrue(result);
        verify(productService, times(1)).existsByIdProduct("P001");
        verify(batchRepository, times(1)).existsByIdProductAndBatch("P001", newBatchDTO.getBatchPurchase());
        verify(batchRepository, times(1)).save(any(Batch.class));
    }

    @Test
    void testAddBatches_ProductNotFound() {
        List<NewBatchDTO> batches = Arrays.asList(newBatchDTO);

        when(productService.existsByIdProduct("P001")).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            batchService.addBatches(batches);
        });

        assertEquals("Producto no encontrado: P001", exception.getMessage());
        verify(productService, times(1)).existsByIdProduct("P001");
        verify(batchRepository, never()).save(any(Batch.class));
    }

    @Test
    void testValidatePurchase_ProductNotFound() {
        List<NewBatchDTO> batches = Arrays.asList(newBatchDTO);

        when(productService.existsByIdProduct("P001")).thenReturn(false);

        ResponseEntity<Object> response = batchService.validatePurchase(batches);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Producto no encontrado: P001", ((Message) response.getBody()).getMessage());
    }

    @Test
    void testValidatePurchase_LoteExistente() {
        List<NewBatchDTO> batches = Arrays.asList(newBatchDTO);

        when(productService.existsByIdProduct("P001")).thenReturn(true);
        when(batchRepository.existsByIdProductAndBatch("P001", newBatchDTO.getBatchPurchase())).thenReturn(true);

        ResponseEntity<Object> response = batchService.validatePurchase(batches);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Producto (P001) y lote (" + newBatchDTO.getBatchPurchase() + ") ya existente en la bd", ((Message) response.getBody()).getMessage());
    }

}
