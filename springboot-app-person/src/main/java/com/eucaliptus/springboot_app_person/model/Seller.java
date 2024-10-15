package com.eucaliptus.springboot_app_person.model;


import jakarta.persistence.*;

@Entity
@Table(name = "sellers")
public class Seller extends Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_seller")
    private Long idSeller;

    @Column(name = "username", nullable = false)
    private String username;

    public Object getUsername() {
        return null;
    }

    public void setUsername(Object username) {
    }

    // Getters y Setters
}
