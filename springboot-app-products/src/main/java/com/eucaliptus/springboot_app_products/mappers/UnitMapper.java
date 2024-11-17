package com.eucaliptus.springboot_app_products.mappers;

import com.eucaliptus.springboot_app_products.dto.UnitDTO;
import com.eucaliptus.springboot_app_products.model.Unit;

public class UnitMapper {

    public static Unit unitDTOToUnit(UnitDTO unitDTO) {
        Unit unit = new Unit();
        unit.setIdUnit(unitDTO.getIdUnit());
        unit.setUnitName(unitDTO.getUnitName().toUpperCase());
        unit.setUnitDescription(unitDTO.getDescription().toUpperCase());
        return unit;
    }

    public static UnitDTO unitToUnitDTO(Unit unit) {
        UnitDTO unitDTO = new UnitDTO();
        unitDTO.setIdUnit(unit.getIdUnit());
        unitDTO.setUnitName(unit.getUnitName());
        unitDTO.setDescription(unit.getUnitDescription());
        return unitDTO;
    }

}
