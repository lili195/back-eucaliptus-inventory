package com.eucaliptus.springboot_app_products.controllers;

import com.eucaliptus.springboot_app_products.dto.UnitDTO;
import com.eucaliptus.springboot_app_products.mappers.UnitMapper;
import com.eucaliptus.springboot_app_products.model.Unit;
import com.eucaliptus.springboot_app_products.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products/units")
public class UnitController {

    @Autowired
    private UnitService unitService;

    @GetMapping("/all")
    public ResponseEntity<List<UnitDTO>> getAllUnits() {
        try {
            List<UnitDTO> units = unitService.getAllUnits().stream()
                    .map(UnitMapper::unitToUnitDTO) // Aquí estás trabajando con la entidad Unit directamente
                    .collect(Collectors.toList());
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/getUnitById/{id}")
    public ResponseEntity<UnitDTO> getUnitById(@PathVariable Long id) {
        try {
            Optional<Unit> optionalUnit = unitService.getUnitById(id); // Cambia a Optional
            if (optionalUnit.isPresent()) {
                UnitDTO unitDTO = UnitMapper.unitToUnitDTO(optionalUnit.get()); // Usa .get() para extraer el valor
                return new ResponseEntity<>(unitDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/addUnit")
    public ResponseEntity<UnitDTO> createUnit(@RequestBody UnitDTO unitDTO) {
        try {
            Unit unit = UnitMapper.unitDTOToUnit(unitDTO);
            Unit savedUnit = unitService.saveUnit(unit);
            return new ResponseEntity<>(UnitMapper.unitToUnitDTO(savedUnit), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateUnit/{id}")
    public ResponseEntity<UnitDTO> updateUnit(@PathVariable Long id, @RequestBody UnitDTO unitDTO) {
        try {
            if (!unitService.existsById(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Unit unit = UnitMapper.unitDTOToUnit(unitDTO);
            unit.setIdUnit(id); // Asigna el ID
            Optional<Unit> updatedUnit = unitService.updateUnit(id, unit); // Devuelve un Optional<Unit>

            if (updatedUnit.isPresent()) {
                // Extrae el Unit del Optional y luego pásalo al mapper
                UnitDTO updatedUnitDTO = UnitMapper.unitToUnitDTO(updatedUnit.get());
                return new ResponseEntity<>(updatedUnitDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/deleteUnit/{id}")
    public ResponseEntity<Void> deleteUnit(@PathVariable Long id) {
        return unitService.deleteUnit(id) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
}
