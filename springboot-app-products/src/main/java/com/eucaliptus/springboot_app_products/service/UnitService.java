package com.eucaliptus.springboot_app_products.service;

import com.eucaliptus.springboot_app_products.dto.UnitDTO;
import com.eucaliptus.springboot_app_products.model.Unit;
import com.eucaliptus.springboot_app_products.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnitService {

    @Autowired
    private UnitRepository unitRepository;

    public List<Unit> getAllUnits() {
        return unitRepository.findAll();
    }

    public Optional<Unit> getUnitById(Long idUnit) {
        return unitRepository.findById(idUnit);
    }

    public Unit saveUnit(Unit unit) {
        return unitRepository.save(unit);
    }

    public Optional<Unit> updateUnit(Long idUnit, Unit unitDetails) {
        return unitRepository.findById(idUnit).map(unit -> {
            unit.setUnitName(unitDetails.getUnitName());
            unit.setDescription(unitDetails.getDescription());
            return unitRepository.save(unit);
        });
    }

    public boolean existsByIdUnit(Long idUnit) {
        return unitRepository.existsById(idUnit);
    }

    public boolean deleteUnit(Long idUnit) {
        return unitRepository.findById(idUnit).map(unit -> {
            unitRepository.delete(unit);
            return true;
        }).orElse(false);
    }

    public boolean existsById(Long id) {
        return unitRepository.existsByIdUnit(id);
    }

}
