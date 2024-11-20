package com.eucaliptus.springboot_app_billing.repository;

import com.eucaliptus.springboot_app_billing.dto.ProductsSaleDTO;
import com.eucaliptus.springboot_app_billing.model.SaleDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

import java.util.Date;
import java.util.List;

@Repository
public interface SaleDetailRepository extends JpaRepository<SaleDetail, Integer> {

    List<SaleDetail> findBySale_IdSale(Integer idBill);

    List<SaleDetail> findByIdProduct(String idProduct);

    boolean existsByIdProductAndSale_IdSale(String idProduct, Integer idBill);

    @Query("SELECT new com.eucaliptus.springboot_app_billing.dto.ProductsSaleDTO(sd.idProduct, SUM(sd.quantitySold), SUM(sd.quantitySold * sd.salePrice)) " +
            "FROM SaleDetail sd " +
            "JOIN sd.sale s " +
            "WHERE s.saleDate BETWEEN :startDate AND :endDate " +
            "GROUP BY sd.idProduct")
    List<ProductsSaleDTO> getSalesSummary(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}