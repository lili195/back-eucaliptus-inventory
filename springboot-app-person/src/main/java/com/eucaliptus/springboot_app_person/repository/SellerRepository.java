package com.eucaliptus.springboot_app_person.repository;

import com.eucaliptus.springboot_app_person.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {

    boolean existsByIdSeller(Long id);
}