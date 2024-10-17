package com.eucaliptus.springboot_app_person.controllers;


import com.eucaliptus.springboot_app_person.dtos.ProviderDTO;
import com.eucaliptus.springboot_app_person.enums.EnumDocumentType;
import com.eucaliptus.springboot_app_person.enums.EnumRole;
import com.eucaliptus.springboot_app_person.mappers.CompanyMapper;
import com.eucaliptus.springboot_app_person.mappers.PersonMapper;
import com.eucaliptus.springboot_app_person.mappers.ProviderMapper;
import com.eucaliptus.springboot_app_person.model.*;
import com.eucaliptus.springboot_app_person.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/person/providers")
public class ProviderController {

    @Autowired
    private ProviderService providerService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PersonService personService;
    @Autowired
    private CompanyService companyService;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllProviders() {
        try {
            return new ResponseEntity<>(providerService.getAllProviders().stream().
                    map(ProviderMapper::providerToProviderDTO).collect(Collectors.toList()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Intente de nuevo mas tarde", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getProviderById/{id}")
    public ResponseEntity<Object> getProviderById(@PathVariable Long id) {
        try{
            if(!providerService.existsById(id))
                return new ResponseEntity<>("Proveedor no encontrado", HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(providerService.getProviderById(id), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Intentalo mas tarde", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addProvider")
    public ResponseEntity<Object> createProvider(@RequestBody ProviderDTO providerDTO) {
        try {
            if(personService.existsByIdPerson(providerDTO.getPersonDTO().getIdPerson()))
                return new ResponseEntity<>("Provider ya existente", HttpStatus.BAD_REQUEST);
            Role role = new Role(EnumRole.valueOf(providerDTO.getPersonDTO().getRole()));
            Person person = PersonMapper.personDTOToPerson(providerDTO.getPersonDTO(), role);
            Company company = CompanyMapper.companyDTOToCompany(providerDTO.getCompanyDTO());
            role = (!roleService.existsRoleByName(role.getNameRole())) ?
                    roleService.saveRole(role) :
                    roleService.getRoleByName(role.getNameRole()).get();
            person.setRole(role);
            person = (!personService.existsByIdPerson(person.getIdNumber())) ?
                    personService.savePerson(person) :
                    personService.getPersonById(person.getIdNumber()).get();
            company = (!companyService.existsByNItCompany(company.getNitCompany()) ?
                    companyService.save(company) :
                    companyService.findById(company.getNitCompany()).get());
            Provider provider = ProviderMapper.providerDTOToProvider(providerDTO, role);
            provider.setPerson(person);
            provider.setCompany(company);
            return new ResponseEntity<>(ProviderMapper.providerToProviderDTO(providerService.saveProvider(provider)), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Intente de nuevo mas tarde", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateProvider/{id}")
    public ResponseEntity<Object> updateProvider(@PathVariable("id") Long idProvider, @RequestBody ProviderDTO providerDetails) {
        try {
            if(!providerService.existsById(idProvider))
                return new ResponseEntity<>("Este proveedor no existe", HttpStatus.BAD_REQUEST);
            Provider provider = ProviderMapper.providerDTOToProvider(providerDetails,
                    roleService.getRoleByName(EnumRole.valueOf(providerDetails.getPersonDTO().getRole())).get());
            Person person = PersonMapper.personDTOToPerson(providerDetails.getPersonDTO(), roleService.getRoleByName(EnumRole.valueOf(providerDetails.getPersonDTO().getRole())).get());
            provider.setPerson(personService.updatePerson(providerDetails.getPersonDTO().getIdPerson(), person).get());
            Company company = CompanyMapper.companyDTOToCompany(providerDetails.getCompanyDTO());
            provider.setCompany(companyService.update(company.getNitCompany(), company).get());
            return new ResponseEntity<>(providerService.updateProvider(idProvider, provider)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build()), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Intente de nuevo mas tarde", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteProvider/{id}")
    public ResponseEntity<Void> deleteProvider(@PathVariable Long id) {
        return providerService.deleteProvider(id) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
}
