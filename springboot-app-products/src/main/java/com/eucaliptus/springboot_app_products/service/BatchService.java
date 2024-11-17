package com.eucaliptus.springboot_app_products.service;

import com.eucaliptus.springboot_app_products.dto.Message;
import com.eucaliptus.springboot_app_products.dto.NewBatchDTO;
import com.eucaliptus.springboot_app_products.model.Batch;
import com.eucaliptus.springboot_app_products.repository.BatchRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.*;

@Service
public class BatchService {

    @Autowired
    private BatchRepository batchRepository;
    @Autowired
    private ProductService productService;

    public List<Batch> getAllBatches() {
        return batchRepository.findAll();
    }

    public Batch saveBatch(Batch batch) {
        batch.setDueDate(this.convertToUTC(batch.getDueDate()));
        batch.setBatch(this.convertToUTC(batch.getBatch()));
        return batchRepository.save(batch);
    }

    public Optional<Batch> updateQuantityAvailable(String idProduct, Date batch, int quantityAvailable) {
        return batchRepository.findByIdProductAndBatch(idProduct, batch).map(batchDetails -> {
            batchDetails.setQuantityAvailableBatch(quantityAvailable);
            return batchRepository.save(batchDetails);
        });
    }

    public Optional<Batch> findByIdProductAndBatch(String idProduct, Date batch) {
        return batchRepository.findByIdProductAndBatch(idProduct, batch);
    }

    public List<Batch> getFirstAvailableBatch(String idProduct) {
        return batchRepository.findAllAvailableBatchByProductId(idProduct);
    }

    public boolean existsByIdProductAndBatch(String idProduct, Date batch) {
        return batchRepository.existsByIdProductAndBatch(idProduct, batch);
    }

    @Transactional
    public boolean addBatches(List<NewBatchDTO> batches){
        for(NewBatchDTO batch : batches){
            if (!productService.existsByIdProduct(batch.getIdProduct()))
                throw new IllegalArgumentException("Producto no encontrado: " + batch.getIdProduct());
            if (existsByIdProductAndBatch(batch.getIdProduct(), batch.getBatchPurchase()))
                throw new IllegalArgumentException("Lote ya existente");
            Batch newBatch = new Batch();
            newBatch.setIdProduct(batch.getIdProduct());
            newBatch.setBatch(batch.getBatchPurchase());
            newBatch.setIdPurchaseDetail(batch.getIdPurchaseDetail());
            newBatch.setQuantityAvailableBatch(batch.getQuantityPurchased());
            newBatch.setDueDate(batch.getPurchaseDueDate());
            saveBatch(newBatch);
        }
        return true;
    }

    public ResponseEntity<Object> validatePurchase(List<NewBatchDTO> batches)  {
        String id = "";
        if (!(id = allProductsExist(batches)).isEmpty())
            return new ResponseEntity<>(new Message("Producto no encontrado: " + id), HttpStatus.NOT_FOUND);
        if (existByProductIdAndBatch(batches.get(batches.size()-1)))
            return new ResponseEntity<>(new Message("Producto (" + batches.get(batches.size()-1).getIdProduct() + ") y lote (" + batches.get(batches.size()-1).getBatchPurchase()+") ya existente en la bd"), HttpStatus.CONFLICT);
        if (thereAreRepeated(batches))
            return new ResponseEntity<>(new Message("Este producto con este lote ya fue añadido a la lista"), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new Message("Compra valida"), HttpStatus.OK);
    }

    private String allProductsExist(List<NewBatchDTO> batches){
        for(NewBatchDTO batch : batches)
            if (!productService.existsByIdProduct(batch.getIdProduct()))
                return batch.getIdProduct();
        return "";
    }

    private boolean existByProductIdAndBatch(NewBatchDTO newBatchDTO) {
        return existsByIdProductAndBatch(newBatchDTO.getIdProduct(), newBatchDTO.getBatchPurchase());
    }

    private boolean thereAreRepeated(List<NewBatchDTO> batches) {
        Set<String> setProductId = new HashSet<>();
        for (NewBatchDTO batch : batches)
            if (!setProductId.add(batch.getIdProduct()+batch.getBatchPurchase()))
                return true;
        return false;
    }

    public Date convertToUTC(Date date) {
        if (date == null)
            return null;
        return Date.from(date.toInstant().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).toInstant());
    }
}
