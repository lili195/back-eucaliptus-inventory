package com.eucaliptus.springboot_app_billing.repository;

import com.eucaliptus.springboot_app_billing.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface SaleRepository extends JpaRepository<Sale, Integer> {

    List<Sale> findByIdBill(Integer idBill);

    List<Sale> findByIdProduct(String idProduct);

    boolean existsByIdProductAndIdBill(String idProduct, Integer idBill);
}