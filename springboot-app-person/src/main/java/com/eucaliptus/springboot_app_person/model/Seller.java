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

    @ManyToOne
    @JoinColumn(name = "id_document_type", referencedColumnName = "id_document_type")
    private DocumentType documentType;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "home_address", nullable = false)
    private String homeAddress;

    public Seller(Person person,
                  DocumentType documentType, String documentNumber, String username, String homeAddress) {
        this.person = person;
        this.person.setIdNumber(documentNumber);
        this.documentType = documentType;
        this.username = username;
        this.homeAddress = homeAddress;
    }
}
