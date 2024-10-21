package com.eucaliptus.springboot_app_person.controllers;


import com.eucaliptus.springboot_app_person.dtos.Message;
import com.eucaliptus.springboot_app_person.dtos.ProviderDTO;
import com.eucaliptus.springboot_app_person.enums.EnumRole;
import com.eucaliptus.springboot_app_person.mappers.CompanyMapper;
import com.eucaliptus.springboot_app_person.mappers.PersonMapper;
import com.eucaliptus.springboot_app_person.mappers.ProviderMapper;
import com.eucaliptus.springboot_app_person.model.*;
import com.eucaliptus.springboot_app_person.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
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
            return new ResponseEntity<>(providerService.getAllActiveProviders().stream().
                    map(ProviderMapper::providerToProviderDTO).collect(Collectors.toList()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getProviderById/{id}")
    public ResponseEntity<Object> getProviderById(@PathVariable Long id) {
        try{
            if(!providerService.existsById(id))
                return new ResponseEntity<>(new Message("Proveedor no encontrado"), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(ProviderMapper.providerToProviderDTO(providerService.getProviderById(id).get()), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(new Message("Intentalo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addProvider")
    public ResponseEntity<Object> createProvider(@RequestBody ProviderDTO providerDTO) {
        try {
            long existId = 0;
            if(personService.existsByIdPerson(providerDTO.getPersonDTO().getIdPerson())) {
                if (personService.getPersonById(providerDTO.getPersonDTO().getIdPerson()).get().isActive()) {
                    return new ResponseEntity<>(new Message("Provider ya existente"), HttpStatus.BAD_REQUEST);
                }
                Optional<Provider> opProvider = providerService.getProviderByPersonId(providerDTO.getPersonDTO().getIdPerson());
                if(opProvider.isPresent()) {
                    existId = opProvider.get().getIdProvider();
                }
            }
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
            person.setActive(true);
            company = (!companyService.existsByNItCompany(company.getNitCompany()) ?
                    companyService.save(company) :
                    companyService.findById(company.getNitCompany()).get());
            Provider provider = ProviderMapper.providerDTOToProvider(providerDTO, role);
            provider.setPerson(person);
            provider.setCompany(company);
            if (existId != 0)
                provider.setIdProvider(existId);
            provider.setActive(true);
            return new ResponseEntity<>(ProviderMapper.providerToProviderDTO(providerService.saveProvider(provider)), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateProvider/{id}")
    public ResponseEntity<Object> updateProvider(@PathVariable("id") Long idProvider, @RequestBody ProviderDTO providerDetails) {
        try {
            if(!providerService.existsById(idProvider))
                return new ResponseEntity<>(new Message("Este proveedor no existe"), HttpStatus.BAD_REQUEST);
            Provider provider = ProviderMapper.providerDTOToProvider(providerDetails,
                    roleService.getRoleByName(EnumRole.valueOf(providerDetails.getPersonDTO().getRole())).get());
            Person person = PersonMapper.personDTOToPerson(providerDetails.getPersonDTO(), roleService.getRoleByName(EnumRole.valueOf(providerDetails.getPersonDTO().getRole())).get());
            provider.setPerson(personService.updatePerson(providerDetails.getPersonDTO().getIdPerson(), person).get());
            Company company = CompanyMapper.companyDTOToCompany(providerDetails.getCompanyDTO());
            provider.setCompany(companyService.update(company.getNitCompany(), company).get());
            return new ResponseEntity<>(ProviderMapper.providerToProviderDTO(providerService.updateProvider(idProvider, provider).get()), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteProvider/{idProvider}")
    public ResponseEntity<Object> deleteProvider(@PathVariable Long idProvider) {
        try {
            if(!providerService.existsById(idProvider))
                return new ResponseEntity<>(new Message("Este proveedor no existe"), HttpStatus.BAD_REQUEST);
            if(providerService.deleteProvider(idProvider))
                return new ResponseEntity<>(new Message("Proveedor eliminado"), HttpStatus.OK);
            return new ResponseEntity<>(new Message("Error con la bd"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e){
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
