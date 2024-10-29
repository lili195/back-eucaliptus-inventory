package com.eucaliptus.springboot_app_products.repository;

import com.eucaliptus.springboot_app_products.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {

    boolean existsByIdUnit(int idUnit);

    Optional<Unit> findByIdUnit(int idUnit);

    Optional<Unit> findByUnitName(String name);

    Optional<Unit> findByUnitNameAndDescription(String unitName, String description);
}
