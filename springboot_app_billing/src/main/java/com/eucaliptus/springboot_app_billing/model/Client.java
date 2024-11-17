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
    @Column(name = "id_client", nullable = false)
    private String idClient;

    @Column(name = "name_client", nullable = false)
    private String nameClient;

    @Column(name = "email_client")
    private String emailClient;
}
