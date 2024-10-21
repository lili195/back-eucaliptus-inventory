package com.eucaliptus.springboot_app_products.repository;

import com.eucaliptus.springboot_app_products.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {

    boolean existsByIdUnit(Long idUnit);

    Optional<Unit> findByIdUnit(Long idUnit);
}
