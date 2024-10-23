package com.eucaliptus.springboot_app_person.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "sellers")
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_seller")
    private Long idSeller;

    @OneToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id_number")
    private Person person;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "home_address", nullable = false)
    private String homeAddress;

    @Column(name = "active")
    private boolean active;

    public Seller(Person person,
                  String documentNumber, String username, String homeAddress) {
        this.person = person;
        this.person.setIdNumber(documentNumber);
        this.username = username;
        this.homeAddress = homeAddress;
        this.active = true;
    }
}
