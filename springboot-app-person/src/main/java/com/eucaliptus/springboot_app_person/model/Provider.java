package com.eucaliptus.springboot_app_person.model;

import com.eucaliptus.springboot_app_person.enums.EnumPersonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Setter
@Getter
@Table(name = "providers")
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_provider")
    private Long idProvider;

    @OneToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id_number")
    private Person person;

    @Column(name = "person_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumPersonType personType;

    @ManyToOne(optional = true)
    @JoinColumn(name = "nit_company", referencedColumnName = "nit_company")
    private Company company;

    @Column(name = "active")
    private boolean active;

    public Provider(Person person,
                    EnumPersonType personType, Company company){;
        this.person = person;
        this.personType = personType;
        this.company = company;
        this.active = true;
    }
}
