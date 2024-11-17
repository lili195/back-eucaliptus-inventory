package com.eucaliptus.springboot_app_products.service;

import com.eucaliptus.springboot_app_products.model.Unit;
import com.eucaliptus.springboot_app_products.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UnitService {

    @Autowired
    private UnitRepository unitRepository;

    public List<Unit> getAllUnits() {
        return unitRepository.findAll();
    }

    public List<String> getDistinctUnitNames(){
        return unitRepository.findDistinctUnitNames();
    }

    public List<String> getDescriptionsByUnitName(String unitName){
        return unitRepository.findDistinctByUnitName(unitName).stream().map(Unit::getUnitDescription).collect(Collectors.toList());
    }

    public Optional<Unit> getUnitById(int idUnit) {
        return unitRepository.findById((long) idUnit);
    }

    public Optional<Unit> getUnitByName(String name) {
        return unitRepository.findByUnitName(name);
    }

    public Optional<Unit> getUnitByNameAndDescription(String name, String description) {
        return unitRepository.findByUnitNameIgnoreCaseAndUnitDescriptionIgnoreCase(name, description);
    }

    public Unit saveUnit(Unit unit) {
        return unitRepository.save(unit);
    }

    public Optional<Unit> updateUnit(Long idUnit, Unit unitDetails) {
        return unitRepository.findById(idUnit).map(unit -> {
            unit.setUnitName(unitDetails.getUnitName());
            unit.setUnitDescription(unitDetails.getUnitDescription());
            return unitRepository.save(unit);
        });
    }

    public boolean existsByIdUnit(int idUnit) {
        return unitRepository.existsById((long) idUnit);
    }

    public boolean deleteUnit(int idUnit) {
        return unitRepository.findById((long) idUnit).map(unit -> {
            unitRepository.delete(unit);
            return true;
        }).orElse(false);
    }

    public boolean existsById(int id) {
        return unitRepository.existsByIdUnit(id);
    }

}
