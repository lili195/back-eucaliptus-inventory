package com.eucaliptus.springboot_app_products.controllers;

import com.eucaliptus.springboot_app_products.dto.Message;
import com.eucaliptus.springboot_app_products.dto.UnitDTO;
import com.eucaliptus.springboot_app_products.mappers.UnitMapper;
import com.eucaliptus.springboot_app_products.model.Unit;
import com.eucaliptus.springboot_app_products.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
                    .map(UnitMapper::unitToUnitDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAllUnitNames")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Object> getAllUnitNames() {
        try {
            return new ResponseEntity<>(unitService.getDistinctUnitNames(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getDescriptionsByUnitName/{unitName}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Object> getDescriptionsByUnitName(@PathVariable("unitName") String unitName) {
        try {
            return new ResponseEntity<>(unitService.getDescriptionsByUnitName(unitName.toUpperCase()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getUnitById/{id}")
    public ResponseEntity<UnitDTO> getUnitById(@PathVariable int id) {
        try {
            Optional<Unit> optionalUnit = unitService.getUnitById(id);
            if (optionalUnit.isPresent()) {
                UnitDTO unitDTO = UnitMapper.unitToUnitDTO(optionalUnit.get());
                return new ResponseEntity<>(unitDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/addUnit")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Object> createUnit(@RequestBody UnitDTO unitDTO) {
        try {
            if (unitService.getUnitByNameAndDescription(unitDTO.getUnitName(), unitDTO.getDescription()).isPresent())
                return new ResponseEntity<>(new Message("Esta unidad ya existe"), HttpStatus.BAD_REQUEST);
            Unit unit = UnitMapper.unitDTOToUnit(unitDTO);
            Unit savedUnit = unitService.saveUnit(unit);
            return new ResponseEntity<>(UnitMapper.unitToUnitDTO(savedUnit), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateUnit/{id}")
    public ResponseEntity<UnitDTO> updateUnit(@PathVariable int id, @RequestBody UnitDTO unitDTO) {
        try {
            if (!unitService.existsByIdUnit(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Unit unit = UnitMapper.unitDTOToUnit(unitDTO);
            unit.setIdUnit(id);
            Optional<Unit> updatedUnit = unitService.updateUnit((long) id, unit);

            if (updatedUnit.isPresent()) {
                UnitDTO updatedUnitDTO = UnitMapper.unitToUnitDTO(updatedUnit.get());
                return new ResponseEntity<>(updatedUnitDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/deleteUnit/{id}")
    public ResponseEntity<Void> deleteUnit(@PathVariable int id) {
        return unitService.deleteUnit(id) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
}
