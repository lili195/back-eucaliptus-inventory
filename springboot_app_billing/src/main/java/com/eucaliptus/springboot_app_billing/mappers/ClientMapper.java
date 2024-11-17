package com.eucaliptus.springboot_app_billing.mappers;


import com.eucaliptus.springboot_app_billing.dto.ClientDTO;
import com.eucaliptus.springboot_app_billing.model.Client;

public class ClientMapper {
    public static Client clientDTOToClient(ClientDTO clientDTO) {
        Client client = new Client();
        client.setIdClient(clientDTO.getIdClient());
        client.setNameClient(clientDTO.getNameClient());
        client.setEmailClient(clientDTO.getEmail());
        return client;
    }

    public static ClientDTO clientToClientDTO(Client client) {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setIdClient(client.getIdClient());
        clientDTO.setNameClient(client.getNameClient());
        clientDTO.setEmail(client.getEmailClient());
        return clientDTO;
    }
}
