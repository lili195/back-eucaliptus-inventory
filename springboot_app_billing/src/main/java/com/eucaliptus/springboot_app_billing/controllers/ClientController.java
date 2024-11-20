package com.eucaliptus.springboot_app_billing.controllers;

import com.eucaliptus.springboot_app_billing.dto.ClientDTO;
import com.eucaliptus.springboot_app_billing.mappers.ClientMapper;
import com.eucaliptus.springboot_app_billing.model.Client;
import com.eucaliptus.springboot_app_billing.service.ClientService;
import com.eucaliptus.springboot_app_products.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/billing/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Object> getAll(){
        try{
            return new ResponseEntity<>(clientService.getAllClients().stream().
                    map(ClientMapper::clientToClientDTO).collect(Collectors.toList()),
                    HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intentalo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getClientById/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Object> getClientById(@PathVariable String id){
        try{
            Optional<Client> opClient = clientService.getClientById(id);
            if (opClient.isEmpty())
                return new ResponseEntity<>(new Message("Cliente no encontrado"), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(ClientMapper.clientToClientDTO(opClient.get()), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intentalo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addClient")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Object> addClient(@RequestBody ClientDTO clientDTO){
        try{
            if (clientService.getClientById(clientDTO.getIdClient()).isPresent())
                return new ResponseEntity<>(new Message("Cliente ya registrado"), HttpStatus.CONFLICT);
            Client client = clientService.saveClient(ClientMapper.clientDTOToClient(clientDTO));
            return new ResponseEntity<>(ClientMapper.clientToClientDTO(client), HttpStatus.CREATED);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intentalo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
