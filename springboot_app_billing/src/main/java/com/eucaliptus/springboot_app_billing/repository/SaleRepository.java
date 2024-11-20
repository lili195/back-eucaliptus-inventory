package com.eucaliptus.springboot_app_billing.repository;

import com.eucaliptus.springboot_app_billing.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Integer> {

    List<Sale> findBySaleDate(Date billDate);

    List<Sale> findByClient_IdClient(String idClient);

    boolean existsByClient_IdClientAndSaleDate(String idClient, Date billDate);

    List<Sale> findByTotalSaleGreaterThan(Double total);
}