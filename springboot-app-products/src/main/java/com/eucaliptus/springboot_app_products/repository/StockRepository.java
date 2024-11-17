package com.eucaliptus.springboot_app_products.repository;

import com.eucaliptus.springboot_app_products.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {


    Optional<Stock> findFirstByProduct_IdProductOrderByModificationDateStockDesc(String idProduct);

    @Query("SELECT s " +
            "FROM Stock s " +
            "WHERE s.modificationDateStock = (" +
            "   SELECT MAX(s2.modificationDateStock) " +
            "   FROM Stock s2 " +
            "   WHERE s2.product.idProduct = s.product.idProduct AND s2.quantityAvailable > 0" +
            ") " +
            "AND s.quantityAvailable > 0")
    List<Stock> findLatestStockByProduct();
}
