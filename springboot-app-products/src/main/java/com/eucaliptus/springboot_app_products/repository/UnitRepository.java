package com.eucaliptus.springboot_app_products.repository;

import com.eucaliptus.springboot_app_products.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {

    @Query("select distinct u.unitName FROM Unit u")
    List<String> findDistinctUnitNames();

    List<Unit> findDistinctByUnitName(String unitName);

    boolean existsByIdUnit(int idUnit);

    Optional<Unit> findByIdUnit(int idUnit);

    Optional<Unit> findByUnitName(String name);

    Optional<Unit> findByUnitNameIgnoreCaseAndUnitDescriptionIgnoreCase(String unitName, String description);
}
