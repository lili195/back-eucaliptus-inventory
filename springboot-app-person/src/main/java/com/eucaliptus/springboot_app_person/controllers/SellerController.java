package com.eucaliptus.springboot_app_person.controllers;

import com.eucaliptus.springboot_app_person.dtos.Message;
import com.eucaliptus.springboot_app_person.dtos.SellerDTO;
import com.eucaliptus.springboot_app_person.dtos.UpdateUserDTO;
import com.eucaliptus.springboot_app_person.enums.EnumDocumentType;
import com.eucaliptus.springboot_app_person.enums.EnumRole;
import com.eucaliptus.springboot_app_person.mappers.PersonMapper;
import com.eucaliptus.springboot_app_person.mappers.SellerMapper;
import com.eucaliptus.springboot_app_person.model.*;
import com.eucaliptus.springboot_app_person.security.JwtTokenUtil;
import com.eucaliptus.springboot_app_person.services.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
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
    private UserService userService;
    @Autowired
    private DocumentTypeService documentTypeService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> getAllSellers() {
        try {
            return new ResponseEntity<>(sellerService.getAllActiveSellers().stream().
                    map(SellerMapper::sellerToSellerDTO).collect(Collectors.toList()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getSellerById/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> getSellerById(@PathVariable Long id) {
        try{
            if(!sellerService.existsById(id))
                return new ResponseEntity<>(new Message("Proveedor no encontrado"), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(SellerMapper.sellerToSellerDTO(sellerService.getSellerById(id).get()), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(new Message("Intentalo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addSeller")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> createSeller(@RequestBody SellerDTO sellerDTO,
                                               HttpServletRequest request) {
        try {
            long existId = 0;
            if(personService.existsByIdPerson(sellerDTO.getPersonDTO().getIdPerson())) {
                if (personService.getPersonById(sellerDTO.getPersonDTO().getIdPerson()).get().isActive())
                    return new ResponseEntity<>(new Message("Persona ya existente"), HttpStatus.BAD_REQUEST);
                Optional<Seller> opSeller = sellerService.getSellerByPersonId(sellerDTO.getPersonDTO().getIdPerson());
                if(opSeller.isPresent())
                    existId = opSeller.get().getIdSeller();
            }
            Role role = new Role(EnumRole.valueOf(sellerDTO.getPersonDTO().getRole()));
            DocumentType documentType = new DocumentType(EnumDocumentType.valueOf(sellerDTO.getPersonDTO().getDocumentType()));
            Person person = PersonMapper.personDTOToPerson(sellerDTO.getPersonDTO(), role, documentType);
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
            Seller seller = SellerMapper.sellerDTOToSeller(sellerDTO, role, documentType);
            seller.setPerson(person);
            if (existId != 0)
                seller.setIdSeller(existId);
            if (!userService.createUser(sellerDTO, userService.getTokenByRequest(request)))
                return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(SellerMapper.sellerToSellerDTO(sellerService.saveSeller(seller)), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateSeller/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> updateSeller(@PathVariable("id") Long idSeller, @RequestBody SellerDTO sellerDetails) {
        try {
            if(!sellerService.existsById(idSeller))
                return new ResponseEntity<>(new Message("Este vendedor no existe"), HttpStatus.BAD_REQUEST);
            Seller seller = SellerMapper.sellerDTOToSeller(sellerDetails,
                    roleService.getRoleByName(EnumRole.valueOf(sellerDetails.getPersonDTO().getRole())).get(),
                    documentTypeService.findByNameType(EnumDocumentType.valueOf(sellerDetails.getPersonDTO().getDocumentType())).get());
            Person person = PersonMapper.personDTOToPerson(sellerDetails.getPersonDTO(),
                    roleService.getRoleByName(EnumRole.valueOf(sellerDetails.getPersonDTO().getRole())).get(),
                    documentTypeService.findByNameType(EnumDocumentType.valueOf(sellerDetails.getPersonDTO().getDocumentType())).get());
            seller.setPerson(personService.updatePerson(sellerDetails.getPersonDTO().getIdPerson(), person).get());
            return new ResponseEntity<>(SellerMapper.sellerToSellerDTO(sellerService.updateSeller(idSeller, seller).get()), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateUserInfo")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Object> updateUserInfo(@RequestBody UpdateUserDTO updateUserDTO, HttpServletRequest request) {
        try{
            String role = jwtTokenUtil.extractAllClaims(userService.getTokenByRequest(request)).get("role", String.class);
            if (!sellerService.existsByUsername(updateUserDTO.getOldUsername()) && !role.equals(EnumRole.ROLE_ADMIN.name()))
                return new ResponseEntity<>(new Message("Este vendedor no existe"), HttpStatus.BAD_REQUEST);
            if (sellerService.existsByUsername(updateUserDTO.getNewUsername()) && !updateUserDTO.getNewUsername().equals(updateUserDTO.getOldUsername()))
                return new ResponseEntity<>(new Message("El username ya existe"), HttpStatus.CONFLICT);
            if (userService.updateUserInfo(updateUserDTO, userService.getTokenByRequest(request))) {
                if (role.equals(EnumRole.ROLE_ADMIN.name())) {
                    Person person = personService.getAdmin().get();
                    person.setEmail(updateUserDTO.getEmail());
                    person.setFirstName(updateUserDTO.getFirstName());
                    person.setLastName(updateUserDTO.getLastName());
                    personService.updatePerson(person.getIdNumber(), person);
                } else {
                    Seller seller = sellerService.getSellerByUsername(updateUserDTO.getOldUsername()).get();
                    seller.setUsername(updateUserDTO.getNewUsername());
                    seller.getPerson().setEmail(updateUserDTO.getEmail());
                    seller.getPerson().setFirstName(updateUserDTO.getFirstName());
                    seller.getPerson().setLastName(updateUserDTO.getLastName());

                    personService.updatePerson(seller.getPerson().getIdNumber(), seller.getPerson()).get();
                    sellerService.updateSeller(seller.getIdSeller(), seller).get();
                }
                return new ResponseEntity<>(new Message("Cuenta actualizada"), HttpStatus.OK);
            }
            return new ResponseEntity<>(new Message("Error al modificar la cuenta"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteSeller/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteSeller(@PathVariable("id") Long idSeller, HttpServletRequest request) {
        try{
            if(!sellerService.existsById(idSeller))
                return new ResponseEntity<>(new Message("Este vendedor no existe"), HttpStatus.BAD_REQUEST);
            if(sellerService.deleteSeller(idSeller, userService.getTokenByRequest(request))
            && userService.deleteUserAccount(sellerService.getSellerById(idSeller).get().getUsername(), userService.getTokenByRequest(request)))
                return new ResponseEntity<>(new Message("Vendedor eliminado"), HttpStatus.OK);
            return new ResponseEntity<>(new Message("Error con la bd"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
