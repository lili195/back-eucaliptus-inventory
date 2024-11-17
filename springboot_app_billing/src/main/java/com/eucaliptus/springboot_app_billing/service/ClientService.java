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

    public Optional<Client> getClientById(String id) {
        return clientRepository.findById(id);
    }

    public Optional<Client> getClientByEmail(String email) {
        return clientRepository.findByEmailClient(email);
    }

    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    public Optional<Client> updateClient(String id, Client clientDetails) {
        return clientRepository.findById(id).map(client -> {
            client.setNameClient(clientDetails.getNameClient());
            client.setEmailClient(clientDetails.getEmailClient());
            return clientRepository.save(client);
        });
    }
}