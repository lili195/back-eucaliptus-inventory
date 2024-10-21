package com.eucaliptus.springboot_app_products.repository;

import com.eucaliptus.springboot_app_products.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    boolean existsByIdStock(Long idStock);

    Optional<Stock> findByIdStock(Long idStock);
}
