package com.eucaliptus.springboot_app_billing.repository;

import com.eucaliptus.springboot_app_billing.model.SaleDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleDetailRepository extends JpaRepository<SaleDetail, Integer> {

    List<SaleDetail> findBySale_IdSale(Integer idBill);

    List<SaleDetail> findByIdProduct(String idProduct);

    boolean existsByIdProductAndSale_IdSale(String idProduct, Integer idBill);
}