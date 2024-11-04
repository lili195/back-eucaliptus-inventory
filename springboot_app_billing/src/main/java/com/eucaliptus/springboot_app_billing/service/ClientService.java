package com.eucaliptus.springboot_app_billing.service;


import com.eucaliptus.springboot_app_billing.model.Client;
import com.eucaliptus.springboot_app_billing.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(Integer id) {
        return clientRepository.findById(id);
    }

    public Optional<Client> getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    public Optional<Client> updateClient(Integer id, Client clientDetails) {
        return clientRepository.findById(id).map(client -> {
            client.setNameClient(clientDetails.getNameClient());
            client.setLastnameClient(clientDetails.getLastnameClient());
            client.setEmail(clientDetails.getEmail());
            return clientRepository.save(client);
        });
    }

    public boolean deleteClient(Integer id) {
        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
            return true;
        }
        return false;
    }
}