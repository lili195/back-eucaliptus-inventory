package com.eucaliptus.springboot_app_billing.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "clients")

public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_client")
    private Integer idClient;

    @Column(name = "name_client", nullable = false)
    private String nameClient;

    @Column(name = "lastname_client", nullable = false)
    private String lastnameClient;

    @Column(name = "email", nullable = false)
    private String email;

    public Client(String nameClient, String lastnameClient, String email) {
        this.nameClient = nameClient;
        this.lastnameClient = lastnameClient;
        this.email = email;
    }
}
