package com.eucaliptus.springboot_app_person.controllers;


import com.eucaliptus.springboot_app_person.dtos.Message;
import com.eucaliptus.springboot_app_person.dtos.ProviderDTO;
import com.eucaliptus.springboot_app_person.enums.EnumDocumentType;
import com.eucaliptus.springboot_app_person.enums.EnumPersonType;
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
    public ResponseEntity<Object> getProviderById(@PathVariable String id) {
        try{
            Optional<Person> opPerson = personService.getActivePersonById(id);
            if (opPerson.isPresent()) {
                if (opPerson.get().getRole().equals(EnumRole.ROLE_PROVIDER))
                    return new ResponseEntity<>(ProviderMapper.providerToProviderDTO(providerService.getProviderById(id).get()), HttpStatus.OK);
                if (opPerson.get().getRole().equals(EnumRole.ROLE_COMPANY))
                    return new ResponseEntity<>(ProviderMapper.providerToProviderDTO(providerService.getProviderByCompanyId(id).get()), HttpStatus.OK);
            }
            return new ResponseEntity<>(new Message("Proveedor no encontrado"), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intentalo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/validateNewProvider")
    public ResponseEntity<Object> validateNewProvider(@RequestBody ProviderDTO providerDTO){
        try{
            if(personService.existsByIdPerson(providerDTO.getPersonDTO().getIdPerson()))
                if (personService.getPersonById(providerDTO.getPersonDTO().getIdPerson()).get().isActive())
                    return new ResponseEntity<>(new Message("Persona ya existente"), HttpStatus.BAD_REQUEST);
            if (providerDTO.getPersonType().equals(EnumPersonType.JURIDICA.name())) {
                if (providerDTO.getCompanyDTO() != null) {
                    Optional<Company> opCompany = companyService.findById(providerDTO.getCompanyDTO().getNit());
                    if (opCompany.isPresent())
                        if (opCompany.get().isActive())
                            return new ResponseEntity<>(new Message("Esta empresa ya se encuentra activa, primero desactivala"), HttpStatus.CONFLICT);
                        else
                            return new ResponseEntity<>(new Message("Esta empresa ya se encuentra registrada, los datos se sobreescribiran"), HttpStatus.FOUND);
                } else {
                    return new ResponseEntity<>(new Message("Sin empresa"), HttpStatus.BAD_REQUEST);
                }
            }
            return new ResponseEntity<>(new Message("OK"), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intenta de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addProvider")
    public ResponseEntity<Object> createProvider(@RequestBody ProviderDTO providerDTO) {
        try {
            if(personService.existsByIdPerson(providerDTO.getPersonDTO().getIdPerson()))
                if (personService.getPersonById(providerDTO.getPersonDTO().getIdPerson()).get().isActive())
                    return new ResponseEntity<>(new Message("Persona ya existente"), HttpStatus.BAD_REQUEST);
            DocumentType documentType = new DocumentType(EnumDocumentType.valueOf(providerDTO.getPersonDTO().getDocumentType()));
            documentType = (!documentTypeService.existsByDocumentType(documentType.getNameType())) ?
                    documentTypeService.saveDocumentType(documentType) :
                    documentTypeService.findByNameType(documentType.getNameType()).get();
            DocumentType documentTypeNIT = new DocumentType(EnumDocumentType.NIT);
            documentTypeNIT = (!documentTypeService.existsByDocumentType(documentTypeNIT.getNameType())) ?
                    documentTypeService.saveDocumentType(documentTypeNIT) :
                    documentTypeService.findByNameType(documentTypeNIT.getNameType()).get();
            Company company = null;
            if (providerDTO.getPersonType().equals(EnumPersonType.JURIDICA.name()))
                if (providerDTO.getCompanyDTO() != null)
                    company = companyService.save(CompanyMapper.companyDTOToCompany(providerDTO.getCompanyDTO(), documentTypeNIT));
            Provider provider = ProviderMapper.providerDTOToProvider(providerDTO, documentType, documentTypeNIT);
            provider.setCompany(company);
            provider.setActive(true);
            return new ResponseEntity<>(ProviderMapper.providerToProviderDTO(providerService.saveProvider(provider)), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateProvider/{id}")
    public ResponseEntity<Object> updateProvider(@PathVariable("id") String idProvider, @RequestBody ProviderDTO providerDetails) {
        try {
            if(!providerService.existsById(idProvider))
                return new ResponseEntity<>(new Message("Este proveedor no existe"), HttpStatus.BAD_REQUEST);
            Provider provider = ProviderMapper.providerDTOToProvider(providerDetails,
                    documentTypeService.findByNameType(EnumDocumentType.valueOf(providerDetails.getPersonDTO().getDocumentType())).get(),
                    documentTypeService.findByNameType(EnumDocumentType.NIT).get());
            if (providerDetails.getPersonType().equals(EnumPersonType.JURIDICA.name())) {
                if (providerDetails.getCompanyDTO() != null) {
                    Company company = CompanyMapper.companyDTOToCompany(providerDetails.getCompanyDTO(), documentTypeService.findByNameType(EnumDocumentType.NIT).get());
                    if (providerService.getProviderById(idProvider).get().getCompany() == null)
                        company = companyService.save(company);
                    provider.setCompany(companyService.update(company.getIdNumber(), company).get());
                }
            }
            return new ResponseEntity<>(ProviderMapper.providerToProviderDTO(providerService.updateProvider(idProvider, provider).get()), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteProvider/{idProvider}")
    public ResponseEntity<Object> deleteProvider(@PathVariable String idProvider) {
        try {
            if(!providerService.existsById(idProvider))
                return new ResponseEntity<>(new Message("Este proveedor no existe"), HttpStatus.BAD_REQUEST);
            Provider provider = providerService.getProviderById(idProvider).get();
            if(providerService.deleteProvider(idProvider) && companyService.deleteCompany(provider.getCompany().getIdNumber()))
                return new ResponseEntity<>(new Message("Proveedor eliminado"), HttpStatus.OK);
            return new ResponseEntity<>(new Message("Error con la bd"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e){
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
