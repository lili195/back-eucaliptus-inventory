package com.eucaliptus.springboot_app_person.repository;

import com.eucaliptus.springboot_app_person.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {

    boolean existsByIdSeller(Long id);

    List<Seller> findByActiveTrue();

    Optional<Seller> findByPerson_IdNumber(String personId);
}