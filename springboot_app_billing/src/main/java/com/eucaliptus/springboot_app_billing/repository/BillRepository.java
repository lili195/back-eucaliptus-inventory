package com.eucaliptus.springboot_app_billing.repository;

import com.eucaliptus.springboot_app_billing.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {

    List<Bill> findByBillDate(Date billDate);

    List<Bill> findByIdClient(Integer idClient);

    boolean existsByIdClientAndBillDate(Integer idClient, Date billDate);

    List<Bill> findByTotalGreaterThan(Double total);
}