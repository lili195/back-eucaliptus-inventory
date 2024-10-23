package com.eucaliptus.springboot_app_person.controllers;


import com.eucaliptus.springboot_app_person.dtos.Message;
import com.eucaliptus.springboot_app_person.dtos.ProviderDTO;
import com.eucaliptus.springboot_app_person.enums.EnumDocumentType;
import com.eucaliptus.springboot_app_person.enums.EnumRole;
import com.eucaliptus.springboot_app_person.mappers.CompanyMapper;
import com.eucaliptus.springboot_app_person.mappers.PersonMapper;
import com.eucaliptus.springboot_app_person.mappers.ProviderMapper;
import com.eucaliptus.springboot_app_person.mappers.SellerMapper;
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
    @Autowired
    private DocumentTypeService documentTypeService;

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
                if (personService.getPersonById(providerDTO.getPersonDTO().getIdPerson()).get().isActive())
                    return new ResponseEntity<>(new Message("Persona ya existente"), HttpStatus.BAD_REQUEST);
                Optional<Provider> opProvider = providerService.getProviderByPersonId(providerDTO.getPersonDTO().getIdPerson());
                if(opProvider.isPresent())
                    existId = opProvider.get().getIdProvider();
            }
            Role role = new Role(EnumRole.valueOf(providerDTO.getPersonDTO().getRole()));
            DocumentType documentType = new DocumentType(EnumDocumentType.valueOf(providerDTO.getPersonDTO().getDocumentType()));
            Person person = PersonMapper.personDTOToPerson(providerDTO.getPersonDTO(), role, documentType);
            role = (!roleService.existsRoleByName(role.getNameRole())) ?
                    roleService.saveRole(role) :
                    roleService.getRoleByName(role.getNameRole()).get();
            person.setRole(role);
            documentType = (!documentTypeService.existsByDocumentType(documentType.getNameType())) ?
                    documentTypeService.saveDocumentType(documentType) :
                    documentTypeService.findByNameType(documentType.getNameType()).get();
            person.setDocumentType(documentType);
            person = personService.savePerson(person);
            person.setActive(true);
            Provider provider = ProviderMapper.providerDTOToProvider(providerDTO, role, documentType);
            if (providerDTO.getCompanyDTO() != null) {
                Company company = CompanyMapper.companyDTOToCompany(providerDTO.getCompanyDTO());
                company = companyService.save(company);
                provider.setCompany(company);
            }
            provider.setPerson(person);
            if (existId != 0)
                provider.setIdProvider(existId);
            provider.setActive(true);
            return new ResponseEntity<>(ProviderMapper.providerToProviderDTO(providerService.saveProvider(provider)), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateProvider/{id}")
    public ResponseEntity<Object> updateProvider(@PathVariable("id") Long idProvider, @RequestBody ProviderDTO providerDetails) {
        try {
            if(!providerService.existsById(idProvider))
                return new ResponseEntity<>(new Message("Este proveedor no existe"), HttpStatus.BAD_REQUEST);
            Provider provider = ProviderMapper.providerDTOToProvider(providerDetails,
                    roleService.getRoleByName(EnumRole.valueOf(providerDetails.getPersonDTO().getRole())).get(),
                    documentTypeService.findByNameType(EnumDocumentType.valueOf(providerDetails.getPersonDTO().getDocumentType())).get());
            Person person = PersonMapper.personDTOToPerson(providerDetails.getPersonDTO(),
                    roleService.getRoleByName(EnumRole.valueOf(providerDetails.getPersonDTO().getRole())).get(),
                    documentTypeService.findByNameType(EnumDocumentType.valueOf(providerDetails.getPersonDTO().getDocumentType())).get());
            provider.setPerson(personService.updatePerson(providerDetails.getPersonDTO().getIdPerson(), person).get());
            if (providerDetails.getCompanyDTO() != null) {
                Company company = CompanyMapper.companyDTOToCompany(providerDetails.getCompanyDTO());
                if (providerService.getProviderById(idProvider).get().getCompany() == null)
                    company = companyService.save(company);
                provider.setCompany(companyService.update(company.getNitCompany(), company).get());
            }
            return new ResponseEntity<>(ProviderMapper.providerToProviderDTO(providerService.updateProvider(idProvider, provider).get()), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
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
