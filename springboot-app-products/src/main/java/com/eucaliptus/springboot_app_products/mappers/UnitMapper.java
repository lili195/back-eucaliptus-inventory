package com.eucaliptus.springboot_app_products.mappers;

import com.eucaliptus.springboot_app_products.dto.UnitDTO;
import com.eucaliptus.springboot_app_products.model.Unit;

import java.util.Optional;

public class UnitMapper {

    public static Unit unitDTOToUnit(UnitDTO unitDTO) {
        Unit unit = new Unit();
        unit.setIdUnit(unitDTO.getIdUnit());
        unit.setUnitName(unitDTO.getUnitName());
        unit.setDescription(unitDTO.getDescription());
        return unit;
    }

    public static UnitDTO unitToUnitDTO(Unit unit) { // Cambia de Optional<Unit> a Unit
        UnitDTO unitDTO = new UnitDTO();
        unitDTO.setIdUnit(unit.getIdUnit());
        unitDTO.setUnitName(unit.getUnitName());
        unitDTO.setDescription(unit.getDescription());
        return unitDTO;
    }

}
