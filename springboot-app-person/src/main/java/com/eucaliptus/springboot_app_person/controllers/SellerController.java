package com.eucaliptus.springboot_app_person.controllers;


import com.eucaliptus.springboot_app_person.dtos.SellerDTO;
import com.eucaliptus.springboot_app_person.enums.EnumDocumentType;
import com.eucaliptus.springboot_app_person.enums.EnumRole;
import com.eucaliptus.springboot_app_person.mappers.CompanyMapper;
import com.eucaliptus.springboot_app_person.mappers.PersonMapper;
import com.eucaliptus.springboot_app_person.mappers.ProviderMapper;
import com.eucaliptus.springboot_app_person.mappers.SellerMapper;
import com.eucaliptus.springboot_app_person.model.*;
import com.eucaliptus.springboot_app_person.services.DocumentTypeService;
import com.eucaliptus.springboot_app_person.services.PersonService;
import com.eucaliptus.springboot_app_person.services.RoleService;
import com.eucaliptus.springboot_app_person.services.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/person/sellers")
public class SellerController {

    @Autowired
    private SellerService sellerService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PersonService personService;
    @Autowired
    private DocumentTypeService documentTypeService;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllSellers() {
        try {
            return new ResponseEntity<>(sellerService.getAllSellers().stream().
                    map(SellerMapper::sellerToSellerDTO).collect(Collectors.toList()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Intente de nuevo mas tarde", HttpStatus.INTERNAL_SERVER_ERROR);
        }    }

    @GetMapping("/getSellerById/{id}")
    public ResponseEntity<Object> getSellerById(@PathVariable Long id) {
        try{
            if(!sellerService.existsById(id))
                return new ResponseEntity<>("Proveedor no encontrado", HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(sellerService.getSellerById(id), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Intentalo mas tarde", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addSeller")
    public ResponseEntity<Object> createSeller(@RequestBody SellerDTO sellerDTO) {
        try {
            if(personService.existsByIdPerson(sellerDTO.getPersonDTO().getIdPerson()))
                return new ResponseEntity<>("Seller ya existente", HttpStatus.BAD_REQUEST);
            Role role = new Role(EnumRole.valueOf(sellerDTO.getPersonDTO().getRole()));
            Person person = PersonMapper.personDTOToPerson(sellerDTO.getPersonDTO(), role);
            DocumentType documentType = new DocumentType(EnumDocumentType.valueOf(sellerDTO.getDocumentType()));
            role = (!roleService.existsRoleByName(role.getNameRole())) ?
                    roleService.saveRole(role) :
                    roleService.getRoleByName(role.getNameRole()).get();
            person.setRole(role);
            person = (!personService.existsByIdPerson(person.getIdNumber())) ?
                    personService.savePerson(person) :
                    personService.getPersonById(person.getIdNumber()).get();
            documentType = (!documentTypeService.existsByDocumentType(documentType.getNameType())) ?
                    documentTypeService.saveDocumentType(documentType) :
                    documentTypeService.findByNameType(documentType.getNameType()).get();
            Seller seller = SellerMapper.sellerDTOToSeller(sellerDTO, role, documentType);
            seller.setPerson(person);
            seller.setDocumentType(documentType);
            return new ResponseEntity<>(SellerMapper.sellerToSellerDTO(sellerService.saveSeller(seller)), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Intente de nuevo mas tarde", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateSeller/{id}")
    public ResponseEntity<Object> updateSeller(@PathVariable("id") Long idSeller, @RequestBody SellerDTO sellerDetails) {
        try {
            if(!sellerService.existsById(idSeller))
                return new ResponseEntity<>("Este proveedor no existe", HttpStatus.BAD_REQUEST);
            Seller seller = SellerMapper.sellerDTOToSeller(sellerDetails,
                    roleService.getRoleByName(EnumRole.valueOf(sellerDetails.getPersonDTO().getRole())).get(),
                    documentTypeService.findByNameType(EnumDocumentType.valueOf(sellerDetails.getDocumentType())).get());
            Person person = PersonMapper.personDTOToPerson(sellerDetails.getPersonDTO(), roleService.getRoleByName(EnumRole.valueOf(sellerDetails.getPersonDTO().getRole())).get());
            seller.setPerson(personService.updatePerson(sellerDetails.getPersonDTO().getIdPerson(), person).get());
            return new ResponseEntity<>(SellerMapper.sellerToSellerDTO(sellerService.updateSeller(idSeller, seller).get()), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Intente de nuevo mas tarde", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteSeller/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) {
        return sellerService.deleteSeller(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
