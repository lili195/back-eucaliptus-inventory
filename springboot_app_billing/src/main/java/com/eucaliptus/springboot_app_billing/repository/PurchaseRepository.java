package com.eucaliptus.springboot_app_billing.repository;

import com.eucaliptus.springboot_app_billing.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

}
