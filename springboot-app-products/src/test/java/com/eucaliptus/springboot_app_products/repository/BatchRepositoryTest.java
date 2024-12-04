package com.eucaliptus.springboot_app_products.repository;

import com.eucaliptus.springboot_app_products.model.Batch;
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
public class BatchRepositoryTest {

    @Mock
    private BatchRepository batchRepository;

    private Batch batch;
    private String productId;
    private Date batchDate;

    @BeforeEach
    void setUp() {
        productId = "P001";
        batchDate = new Date();
        batch = new Batch(100, batchDate, new Date());
    }

    @Test
    void testExistsByIdProductAndBatch() {
        when(batchRepository.existsByIdProductAndBatch(productId, batchDate)).thenReturn(true);

        boolean exists = batchRepository.existsByIdProductAndBatch(productId, batchDate);

        assertTrue(exists);
        verify(batchRepository, times(1)).existsByIdProductAndBatch(productId, batchDate);
    }

    @Test
    void testFindByIdProductAndBatch() {
        when(batchRepository.findByIdProductAndBatch(productId, batchDate)).thenReturn(Optional.of(batch));

        Optional<Batch> foundBatch = batchRepository.findByIdProductAndBatch(productId, batchDate);

        assertTrue(foundBatch.isPresent());
        assertEquals(productId, foundBatch.get().getIdProduct());
        assertEquals(batchDate, foundBatch.get().getBatch());
        verify(batchRepository, times(1)).findByIdProductAndBatch(productId, batchDate);
    }

    @Test
    void testFindAllAvailableBatchByProductId() {
        when(batchRepository.findAllAvailableBatchByProductId(productId)).thenReturn(List.of(batch));

        List<Batch> availableBatches = batchRepository.findAllAvailableBatchByProductId(productId);

        assertNotNull(availableBatches);
        assertFalse(availableBatches.isEmpty());
        assertEquals(productId, availableBatches.get(0).getIdProduct());
        verify(batchRepository, times(1)).findAllAvailableBatchByProductId(productId);
    }
}
