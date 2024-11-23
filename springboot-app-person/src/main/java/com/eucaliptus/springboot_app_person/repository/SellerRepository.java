package com.eucaliptus.springboot_app_person.repository;

import com.eucaliptus.springboot_app_person.enums.EnumRole;
import com.eucaliptus.springboot_app_person.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, String> {

    boolean existsByIdNumber(String id);

    boolean existsByUsername(String Username);

    List<Seller> findByActiveTrueAndRole(EnumRole role);

    Optional<Seller> findByIdNumber(String personId);

    Optional<Seller> getByUsername(String username);
}