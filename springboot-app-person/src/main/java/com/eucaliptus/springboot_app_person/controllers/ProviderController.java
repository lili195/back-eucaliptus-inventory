package com.eucaliptus.springboot_app_person.controllers;


import com.eucaliptus.springboot_app_person.dtos.Message;
import com.eucaliptus.springboot_app_person.dtos.ProviderDTO;
import com.eucaliptus.springboot_app_person.enums.EnumDocumentType;
import com.eucaliptus.springboot_app_person.enums.EnumPersonType;
import com.eucaliptus.springboot_app_person.enums.EnumRole;
import com.eucaliptus.springboot_app_person.mappers.CompanyMapper;
import com.eucaliptus.springboot_app_person.mappers.ProviderMapper;
import com.eucaliptus.springboot_app_person.model.*;
import com.eucaliptus.springboot_app_person.services.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controlador REST para gestionar las operaciones relacionadas con los proveedores.
 * <p>
 * Este controlador está protegido por el rol "ROLE_ADMIN", y permite realizar diversas operaciones
 * como obtener proveedores, validar y crear nuevos proveedores, actualizar y eliminar proveedores.
 * </p>
 */
@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/person/providers")
public class ProviderController {

    @Autowired
    private ProviderService providerService;
    @Autowired
    private PersonService personService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private APIService apiService;
    @Autowired
    private DocumentTypeService documentTypeService;

    /**
     * Obtiene todos los proveedores activos.
     *
     * @return una respuesta con una lista de todos los proveedores activos en formato DTO.
     */
    @GetMapping("/all")
    public ResponseEntity<Object> getAllProviders() {
        try {
            return new ResponseEntity<>(providerService.getAllActiveProviders().stream().
                    map(ProviderMapper::providerToProviderDTO).collect(Collectors.toList()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene un proveedor por su ID.
     * Si el proveedor es una persona o una empresa, realiza la búsqueda correspondiente.
     *
     * @param id el ID del proveedor.
     * @return una respuesta con el proveedor encontrado o un mensaje de error si no se encuentra.
     */
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

    /**
     * Obtiene un proveedor (persona o empresa) por su ID.
     *
     * @param id el ID del proveedor.
     * @return una respuesta con el proveedor encontrado o un mensaje de error si no se encuentra.
     */
    @GetMapping("/getProvider/{id}")
    public ResponseEntity<Object> getProvider(@PathVariable String id) {
        try{
            Optional<Person> opPerson = personService.getPersonById(id);
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

    /**
     * Obtiene una empresa por su NIT.
     *
     * @param nit el NIT de la empresa.
     * @return una respuesta con los datos de la empresa si se encuentra, o un mensaje de error si no.
     */
    @GetMapping("/getCompanyByNit/{nit}")
    public ResponseEntity<Object> getCompanyByNit(@PathVariable String nit){
        try{
            Optional<Company> opCompany = companyService.findByNit(nit);
            if (opCompany.isPresent())
                return new ResponseEntity<>(CompanyMapper.companyToCompanyDTO(opCompany.get()), HttpStatus.OK);
            return new ResponseEntity<>(new Message("Empresa no encontrada"), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intentalo de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Valida si es posible crear un nuevo proveedor.
     * Verifica si la persona ya existe y si la empresa (si corresponde) ya está registrada.
     *
     * @param providerDTO los datos del nuevo proveedor.
     * @return una respuesta con el resultado de la validación.
     */
    @PostMapping("/validateNewProvider")
    public ResponseEntity<Object> validateNewProvider(@RequestBody ProviderDTO providerDTO) {
        try {
            if (personService.existsByIdPerson(providerDTO.getPersonDTO().getIdPerson()))
                if (personService.getPersonById(providerDTO.getPersonDTO().getIdPerson()).get().isActive())
                    return new ResponseEntity<>(new Message("Persona ya existente"), HttpStatus.BAD_REQUEST);
            if (providerDTO.getPersonType().equals(EnumPersonType.JURIDICA.name())) {
                if (providerDTO.getCompanyDTO() != null) {
                    Optional<Company> opCompany = companyService.findByNit(providerDTO.getCompanyDTO().getNit());
                    if (opCompany.isPresent())
                        return new ResponseEntity<>(new Message("Esta empresa ya se encuentra registrada, los datos se sobreescribiran"), HttpStatus.FOUND);
                } else {
                    return new ResponseEntity<>(new Message("Sin empresa"), HttpStatus.BAD_REQUEST);
                }
            }
            return new ResponseEntity<>(new Message("OK"), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intenta de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Crea un nuevo proveedor.
     * Si el proveedor es de tipo jurídica, se asocia una empresa a este proveedor.
     *
     * @param providerDTO los datos del proveedor a crear.
     * @return una respuesta con los datos del proveedor creado.
     */
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
            Provider provider = ProviderMapper.providerDTOToProvider(providerDTO, documentType);
            if (providerDTO.getPersonType().equals(EnumPersonType.JURIDICA.name()))
                if (providerDTO.getCompanyDTO() != null)
                    provider.setCompany(companyService.save(CompanyMapper.companyDTOToCompany(providerDTO.getCompanyDTO())));
            provider.setActive(true);
            return new ResponseEntity<>(ProviderMapper.providerToProviderDTO(providerService.saveProvider(provider)), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Actualiza la información de un proveedor existente.
     *
     * @param idProvider el ID del proveedor a actualizar.
     * @param providerDetails los nuevos detalles del proveedor.
     * @return una respuesta con los datos del proveedor actualizado.
     */
    @PutMapping("/updateProvider/{id}")
    public ResponseEntity<Object> updateProvider(@PathVariable("id") String idProvider, @RequestBody ProviderDTO providerDetails) {
        try {
            if(!providerService.existsById(idProvider))
                return new ResponseEntity<>(new Message("Este proveedor no existe"), HttpStatus.BAD_REQUEST);
            Provider provider = ProviderMapper.providerDTOToProvider(providerDetails,
                    documentTypeService.findByNameType(EnumDocumentType.valueOf(providerDetails.getPersonDTO().getDocumentType())).get());
            if (providerDetails.getPersonType().equals(EnumPersonType.JURIDICA.name())) {
                if (providerDetails.getCompanyDTO() != null) {
                    Company company = CompanyMapper.companyDTOToCompany(providerDetails.getCompanyDTO());
                    if (providerService.getProviderById(idProvider).get().getCompany() == null)
                        company = companyService.save(company);
                    provider.setCompany(companyService.update(company.getNitCompany(), company).get());
                }
            }
            return new ResponseEntity<>(ProviderMapper.providerToProviderDTO(providerService.updateProvider(idProvider, provider).get()), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Elimina un proveedor dado su ID.
     *
     * @param request el ID del proveedor a eliminar.
     * @return una respuesta con el resultado de la eliminación.
     */
    @DeleteMapping("/deleteProvider/{idProvider}")
    public ResponseEntity<Object> deleteProvider(@PathVariable String idProvider, HttpServletRequest request) {
        try {
            if (!providerService.existsById(idProvider))
                return new ResponseEntity<>(new Message("Este proveedor no existe"), HttpStatus.BAD_REQUEST);
            if (providerService.deleteProviderAndProducts(idProvider, apiService.getTokenByRequest(request)))
                return new ResponseEntity<>(new Message("Proveedor eliminado"), HttpStatus.OK);
            return new ResponseEntity<>(new Message("Error con la bd"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
