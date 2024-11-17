package com.eucaliptus.springboot_app_products.repository;

import com.eucaliptus.springboot_app_products.model.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface BatchRepository extends JpaRepository<Batch, String> {

    boolean existsByIdProductAndBatch(String productId, Date batch);

    Optional<Batch> findByIdProductAndBatch(String productId, Date batch);
}
